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
  graphHeight: number= 600;

  constructor(private analysisService: AnalysisService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const analysisId = BigInt(params.get('id')!);
      this.analysisService.getAnalysis(analysisId).subscribe({
        next: (response) => {
          this.getAnalysisResponse = response;
          this.prepareChartDataPPC();
          this.prepareGraphFCL();
        },
        error: (error) => {
          window.alert(`Status: ${error.status}\nErrors: ${error.error.errors.join(',\n')}`);
        }
      })
    });
  }

  prepareChartDataPPC(): void {
    this.chartDataPPC = this.getAnalysisResponse!.percentagesPerCluster.map(ppc => ({
      name: `Cluster ${ppc.clusterLabel}`,
      value: ppc.percentageInProject,
    }));

    // Initial values
    this.topClustersValue = this.chartDataPPC.length;
    this.lastClusterValue = 0;
  }

  prepareGraphFCL(): void {
    const clusterResults: ClusterResultDto[] = this.getAnalysisResponse!.analysisData.clusterResults!;

    this.nodesFCL = clusterResults.map((cr, index) => {
        const x = Math.min(50 * index, this.graphWidth - 50);
        const y = 100 + 50 * index;
        const node: Node = {
          id: cr.id.toString(),
          label: `${cr.filename}`,
          data: {
            filepath: cr.filepath,
            clusterLabel: cr.clusterLabel,
            customColor: this.getClusterColor(cr.clusterLabel)
          },
          position: {x: x, y: y}
        };
        return node;
    });
  }

  getClusterColor(clusterLabel: number): string {
    return this.clusterColors[clusterLabel % this.clusterColors.length];
  }
}
