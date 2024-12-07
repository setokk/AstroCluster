import { HttpClient } from "@angular/common/http";
import { Api } from "./api";
import { Injectable } from "@angular/core";
import {Observable} from "rxjs";
import {GetAnalysisResponse} from "../response/GetAnalysisResponse";
import {AuthService} from "./auth-service";
import {GetLatestAnalysesResponse} from "../response/GetLatestAnalysesResponse";

@Injectable({providedIn: 'root'})
export class AnalysisService {
    constructor(private authService: AuthService, private http: HttpClient) {}

    getAnalysis(analysisId: bigint): Observable<GetAnalysisResponse> {
        return this.http.get<GetAnalysisResponse>(`${Api.ANALYSIS_ENDPOINT}/${analysisId}`, {
            headers: this.authService.getHttpHeaders()
        });
    }

    getLatestAnalyses(): Observable<GetLatestAnalysesResponse> {
        return this.http.get<GetLatestAnalysesResponse>(Api.ANALYSIS_LATEST_ANALYSES_ENDPOINT, {
            headers: this.authService.getHttpHeaders()
        });
    }
}