import { Component, AfterViewInit, Input, ElementRef, ViewChild } from '@angular/core';
import {GetAnalysisResponse} from "../core/response/GetAnalysisResponse";
import {ClusterResultDto} from "../core/model/ClusterResultDto";
import * as d3 from 'd3';

@Component({
  selector: 'app-d3-graph-analysis',
  standalone: true,
  template: '<div #chart></div>',
  styleUrls: ['./d3-graph-analysis.component.css']
})
export class D3GraphAnalysisComponent implements AfterViewInit  {
  @ViewChild('chart') private chartContainer!: ElementRef;
  @Input() getAnalysisResponse?: GetAnalysisResponse;
  @Input() graphWidth: number = 800;
  @Input() graphHeight: number = 400;

  ngAfterViewInit(): void {
    const clusterResults: ClusterResultDto[] = this.getAnalysisResponse!.analysisData.clusterResults!;
    this.createAnalysisGraph(clusterResults);
  }

  private createAnalysisGraph(clusterResults: ClusterResultDto[]): void {
    const svg = d3.select<SVGSVGElement, unknown>(this.chartContainer.nativeElement)
      .append('svg')
      .attr('width', this.graphWidth)
      .attr('height', this.graphHeight)
      .call(d3.zoom<SVGSVGElement, unknown>().on('zoom', (event) => {
        svg.attr('transform', event.transform);
      }))
      .append('g');

    const groups = d3.group(clusterResults, (cr: ClusterResultDto) => cr.clusterLabel);
    const color = d3.scaleOrdinal(d3.schemeCategory10);

    const padding = 500;
    const maxRadius = 400;
    let groupIndex = 0;
    groups.forEach((nodes: ClusterResultDto[], group: number) => {
      const angleStep = (2 * Math.PI) / nodes.length;
      const maxFilenameLength = Math.max(...nodes.map(node => node.filename.length));
      const radius = Math.max(30, maxFilenameLength * 5, maxRadius);
      const centerX = (groupIndex % 4) * (radius * 2 + padding) + radius + padding / 2;
      const centerY = Math.floor(groupIndex / 4) * (radius * 2 + padding) + radius + padding / 2;

      nodes.forEach((node: ClusterResultDto, index: number) => {
        const angle = index * angleStep;
        node.x! = centerX + radius * Math.cos(angle);
        node.y! = centerY + radius * Math.sin(angle);
      });

      groupIndex++;
    });

    svg.selectAll<SVGCircleElement, ClusterResultDto>('circle')
      .data(clusterResults)
      .enter()
      .append('circle')
      .attr('cx', (cr: ClusterResultDto) => cr.x!)
      .attr('cy', (cr: ClusterResultDto) => cr.y!)
      .attr('r', 10)
      .attr('stroke', 'white')
      .attr('fill', (cr: ClusterResultDto) => color(cr.clusterLabel.toString()));

    svg.selectAll<SVGTextElement, ClusterResultDto>('.clusterLabel')
      .data(clusterResults)
      .enter()
      .append('text')
      .attr('class', 'clusterLabel')
      .attr('x', (cr: ClusterResultDto) => cr.x!)
      .attr('y', (cr: ClusterResultDto) => cr.y! + 4)
      .attr('text-anchor', 'middle')
      .attr('font-size', '10px')
      .attr('fill', 'black')
      .text((cr: ClusterResultDto) => cr.clusterLabel);

    svg.selectAll<SVGTextElement, ClusterResultDto>('.label')
      .data(clusterResults)
      .enter()
      .append('text')
      .attr('class', 'label')
      .attr('x', (cr: ClusterResultDto) => cr.x!)
      .attr('y',(cr: ClusterResultDto) => cr.y! + 22)
      .attr('text-anchor', 'middle')
      .attr('font-size', '10px')
      .attr('fill', 'white')
      .text((cr: ClusterResultDto) => cr.filename);
  }
}
