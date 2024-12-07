import {Component} from '@angular/core';
import {GetAnalysisResponse} from "../core/response/GetAnalysisResponse";
import {AnalysisService} from "../core/services/analysis-service";
import {ActivatedRoute} from "@angular/router";
import {Color, NgxChartsModule, ScaleType} from "@swimlane/ngx-charts";

@Component({
  selector: 'app-analysis-result',
  standalone: true,
  imports: [
    NgxChartsModule
  ],
  templateUrl: './analysis-result.component.html',
  styleUrl: './analysis-result.component.css'
})
export class AnalysisResultComponent {
  getAnalysisResponse?: GetAnalysisResponse;

  activeChartTitle: string = 'Percentage per Cluster';

  // Percentage per Cluster Chart
  chartDataPPC: { name: string; value: number; }[] = [];
  colorSchemePPC: Color = {
    name: 'PPC Color Scheme',
    selectable: true,
    group: ScaleType.Linear,
    domain: ['#3d97f2', '#e38136', '#dacb4e', '#AAAAAA']
  };

  constructor(private analysisService: AnalysisService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const analysisId = BigInt(params.get('id')!);
      this.analysisService.getAnalysis(analysisId).subscribe({
        next: (response) => {
          this.getAnalysisResponse = response;
          this.prepareChartDataPPC();
        },
        error: (error) => {
          window.alert(`Status: ${error.status}\nErrors: ${error.error.errors.join(',\n')}`);
        }
      })
    });
  }

  prepareChartDataPPC(): void {
    this.chartDataPPC = this.getAnalysisResponse!.percentagesPerCluster.map((ppc) => ({
      name: `Cluster ${ppc.clusterLabel}`,
      value: ppc.percentageInProject,
    }));
  }
}
