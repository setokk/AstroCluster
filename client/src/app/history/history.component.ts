import { Component } from '@angular/core';
import {AnalysisService} from "../core/services/analysis-service";
import {GetLatestAnalysesResponse} from "../core/response/GetLatestAnalysesResponse";
import {AuthService} from "../core/services/auth-service";
import {DateUtils} from "../core/util/date-utils";
import {Router} from "@angular/router";

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [],
  templateUrl: './history.component.html',
  styleUrl: './history.component.css'
})
export class HistoryComponent {
  latestAnalysesResponse?: GetLatestAnalysesResponse;

  constructor(private analysisService: AnalysisService,
              protected authService: AuthService,
              private router: Router) {}

  ngOnInit(): void {
    this.analysisService.getLatestAnalyses().subscribe({
      next: (response) => {
        this.latestAnalysesResponse = response;
      },
      error: (error) => {
        window.alert(`Status: ${error.status}\nErrors: ${error.error.errors.join(',\n')}`);
      }
    });
  }

  onClickAnalysisListItem(analysisId: bigint) {
    this.router.navigate([`/analysis/${analysisId}`]);
  }

  protected readonly DateUtils = DateUtils;
}
