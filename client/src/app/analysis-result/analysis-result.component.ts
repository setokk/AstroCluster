import {Component} from '@angular/core';
import {GetAnalysisResponse} from "../core/response/GetAnalysisResponse";
import {AnalysisService} from "../core/services/analysis-service";
import {ActivatedRoute} from "@angular/router";
import {Color, NgxChartsModule, ScaleType} from "@swimlane/ngx-charts";
import {FormsModule, NgForm} from "@angular/forms";
import {NgxGraphModule, Node} from "@swimlane/ngx-graph";
import {ClusterResultDto} from "../core/model/ClusterResultDto";
import {DropdownListComponent} from "../dropdown-list/dropdown-list.component";
import {D3GraphAnalysisComponent} from "../d3-graph-analysis/d3-graph-analysis.component";
import {SimilarFilesCriteriaEnum} from "../core/enums/similar-files-criteria-enum";
import {InterestPdfAnalysisRequest} from "../core/request/InterestPdfAnalysisRequest";
import {DateUtils} from "../core/util/date-utils";
import {Observable} from "rxjs";
import {HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-analysis-result',
  standalone: true,
  imports: [
    NgxChartsModule,
    FormsModule,
    NgxGraphModule,
    DropdownListComponent,
    D3GraphAnalysisComponent
  ],
  templateUrl: './analysis-result.component.html',
  styleUrl: './analysis-result.component.css'
})
export class AnalysisResultComponent {
  getAnalysisResponse?: GetAnalysisResponse;
  interestPdfAnalysisRequest: InterestPdfAnalysisRequest = {
    analysisId: undefined,
    similarFilesCriteria: undefined,
    isDescriptive: false,
    avgPerGenerationLOC: undefined,
    perHourLOC: undefined,
    perHourSalary: undefined
  };

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
  graphX: any = { minX: -200, maxX: 482 };
  graphY: any = { minY: 0, maxY: 370 };

  pressedButton?: string;
  isFullscreen: boolean = false;
  similarFilesCriteria = SimilarFilesCriteriaEnum.entries();

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

    const horizontalSpacing = 30;
    const verticalSpacing = 30;
    const totalX = Math.abs(this.graphX.minX) + Math.abs(this.graphX.maxX);
    const totalY = Math.abs(this.graphX.minY) + Math.abs(this.graphX.maxY);
    const maxNodesPerRow = Math.floor(totalX / horizontalSpacing);

    this.nodesFCL = clusterResults.map((cr, index) => {
      const row = Math.floor(index / maxNodesPerRow);
      const col = index % maxNodesPerRow;
      const x = this.graphX.minX + col * horizontalSpacing;
      const y = this.graphY.minY + row * verticalSpacing

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

  onDropdownChange(field: string, value: string) {
    (this.interestPdfAnalysisRequest as any)[field] = value; // Dynamically update form model
  }

  onAnalysisFileDownloadSubmit(form: NgForm) {
    this.interestPdfAnalysisRequest.analysisId = this.getAnalysisResponse?.analysisData.id;
    this.interestPdfAnalysisRequest.isDescriptive = (form.form.value.isDescriptive === '') ? false : form.form.value.isDescriptive;
    this.interestPdfAnalysisRequest.avgPerGenerationLOC = form.form.value.avgPerGenerationLOC;
    this.interestPdfAnalysisRequest.perHourLOC = form.form.value.perHourLOC;
    this.interestPdfAnalysisRequest.perHourSalary = form.form.value.perHourSalary;

    let observableBLOB: Observable<HttpResponse<Blob>> = new Observable();
    switch (this.pressedButton) {
      case 'PDF': {
        observableBLOB = this.analysisService.downloadInterestAnalysisPdf(this.interestPdfAnalysisRequest);
        break;
      }
      case 'CSV': {
        observableBLOB = this.analysisService.downloadInterestAnalysisCsv(this.interestPdfAnalysisRequest);
        break;
      }
    }

    observableBLOB.subscribe({
      next: (response) => {
        const contentDisposition = response.headers.get('Content-Disposition') || '';
        const filename = this.getFileNameFromContentDisposition(contentDisposition) || 'interest';

        const blob = response.body as Blob;
        const downloadUrl = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.download = filename;
        link.click();
        window.URL.revokeObjectURL(downloadUrl);
      },
      error: (error) => {
        window.alert(`Status: ${error.status}\nErrors: ${error.error.errors.join(',\n')}`);
      }
    }
    );
  }

  private getFileNameFromContentDisposition(contentDisposition: string): string | null {
    const match = contentDisposition.match(/(?:^|;)\s*filename="([^"]+)"/);
    return match ? match[1] : null;
  }

  getClusterColor(clusterLabel: number): string {
    return this.clusterColors[clusterLabel % this.clusterColors.length];
  }

  translate(x: number, y: number): string {
    return `translate(${x}, ${y})`;
  }

  protected readonly DateUtils = DateUtils;

  setPressedButton(pressedButton: string): void {
    this.pressedButton = pressedButton;
  }

  toggleGraphFullscreen(): void {
    this.isFullscreen = !this.isFullscreen;
  }
}
