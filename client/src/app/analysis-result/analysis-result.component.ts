import {Component} from '@angular/core';
import {GetAnalysisResponse} from "../core/response/GetAnalysisResponse";
import {AnalysisService} from "../core/services/analysis-service";
import {ActivatedRoute} from "@angular/router";
import {Color, NgxChartsModule, ScaleType} from "@swimlane/ngx-charts";
import {FormsModule} from "@angular/forms";
import {NgxGraphModule, Node} from "@swimlane/ngx-graph";
import {ClusterResultDto} from "../core/model/ClusterResultDto";

@Component({
  selector: 'app-analysis-result',
  standalone: true,
  imports: [
    NgxChartsModule,
    FormsModule,
    NgxGraphModule
  ],
  templateUrl: './analysis-result.component.html',
  styleUrl: './analysis-result.component.css'
})
export class AnalysisResultComponent {
  getAnalysisResponse?: GetAnalysisResponse;

  activeChartTitle: string = 'Cluster File Results';
  clusterColors: string[] = ['#3d97f2', '#e38136', '#dacb4e', '#7fe6b9', '#bff23d', '#7d4bff', '#ea4932', '#dcb3af'];

  // Percentage per Cluster Chart
  chartDataPPC: { name: string; value: number; }[] = [];
  colorSchemePPC: Color = {
    name: 'PPC Color Scheme',
    selectable: true,
    group: ScaleType.Linear,
    domain: this.clusterColors
  };
  topClustersValue?: number;
  lastClusterValue?: number;

  // Graph for all files->cluster labels
  nodesFCL: Node[] = [];
  graphWidth: number = 800;
  graphHeight: number= 400;

  constructor(private analysisService: AnalysisService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const analysisId = BigInt(params.get('id')!);
      this.analysisService.getAnalysis(analysisId).subscribe({
        next: (response) => {
          this.getAnalysisResponse = response;
          this.onClickReset();
          this.prepareGraphFCL();
        },
        error: (error) => {
          window.alert(`Status: ${error.status}\nErrors: ${error.error.errors.join(',\n')}`);
        }
      })
    });
  }

  prepareChartDataPPC(): void {
    const numOfFiles: number = this.getAnalysisResponse!.analysisData.clusterResults!.length;
    this.chartDataPPC = this.getAnalysisResponse!.percentagesPerCluster.map(ppc => ({
      name: `Cluster ${ppc.clusterLabel} (${Math.floor(numOfFiles * ppc.percentageInProject / 100)} files)`,
      value: ppc.percentageInProject,
    })).sort((a, b) => b.value - a.value);
  }

  prepareGraphFCL(): void {
    const clusterResults: ClusterResultDto[] = this.getAnalysisResponse!.analysisData.clusterResults!;

    const horizontalSpacing = 50;
    const verticalSpacing = 50;
    const maxNodesPerRow = Math.floor(this.graphWidth / horizontalSpacing);
    this.nodesFCL = clusterResults.map((cr, index) => {
      const row = Math.floor(index / maxNodesPerRow);
      const col = index % maxNodesPerRow;
      const x = col * horizontalSpacing;
      const y = 100 + row * verticalSpacing

      const node: Node = {
        id: cr.id.toString(),
        label: `${cr.filename}`,
        data: {
          filepath: cr.filepath,
          clusterLabel: cr.clusterLabel,
          customColor: this.getClusterColor(cr.clusterLabel),
          customX: x,
          customY: y
        }
      };
      return node;
    });
  }

  onClickCurrentlyShowing(): void {
    this.activeChartTitle = (this.activeChartTitle === 'Percentages per Cluster') ? 'Cluster File Results' : 'Percentages per Cluster';
  }

  onChangeTopClustersValuePPC(event: Event): void {
    const newTopClustersValue: number = (event.target as HTMLInputElement).valueAsNumber;
    const numOfFiles: number = this.getAnalysisResponse!.analysisData.clusterResults!.length;
    const intermediateChartData: any = this.getAnalysisResponse!.percentagesPerCluster.map(ppc => ({
      name: `Cluster ${ppc.clusterLabel} (${Math.floor(numOfFiles * ppc.percentageInProject / 100)} files)`,
      value: ppc.percentageInProject,
    }));
    this.chartDataPPC = intermediateChartData
        .sort((a: any, b: any) => b.value - a.value) // DESCENDING
        .slice(0, newTopClustersValue);
  }

  onChangeLastClustersValuePPC(event: Event): void {
    const newLastClusterValue: number = (event.target as HTMLInputElement).valueAsNumber;
    const numOfFiles: number = this.getAnalysisResponse!.analysisData.clusterResults!.length;
    const intermediateChartData: any = this.getAnalysisResponse!.percentagesPerCluster.map(ppc => ({
      name: `Cluster ${ppc.clusterLabel} (${Math.floor(numOfFiles * ppc.percentageInProject / 100)} files)`,
      value: ppc.percentageInProject,
    }));
    this.chartDataPPC = intermediateChartData
        .sort((a: any, b: any) => a.value - b.value) // ASCENDING
        .slice(0, newLastClusterValue);
  }

  onClickReset(): void {
    this.topClustersValue = this.getAnalysisResponse!.percentagesPerCluster.length;
    this.lastClusterValue = 1;
    this.prepareChartDataPPC();
  }

  getClusterColor(clusterLabel: number): string {
    return this.clusterColors[clusterLabel % this.clusterColors.length];
  }

  translate(x: number, y: number): string {
    return `translate(${x}, ${y})`;
  }
}
