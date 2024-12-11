import {HttpClient, HttpResponse} from "@angular/common/http";
import { Api } from "./api";
import { Injectable } from "@angular/core";
import {Observable} from "rxjs";
import {GetAnalysisResponse} from "../response/GetAnalysisResponse";
import {AuthService} from "./auth-service";
import {GetLatestAnalysesResponse} from "../response/GetLatestAnalysesResponse";
import {InterestPdfAnalysisRequest} from "../request/InterestPdfAnalysisRequest";

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

    downloadInterestAnalysisPdf(interestPdfAnalysisRequest: InterestPdfAnalysisRequest): Observable<HttpResponse<Blob>> {
        return this.http.post<HttpResponse<Blob>>(Api.ANALYSIS_INTEREST_PDF, interestPdfAnalysisRequest, {
            headers: this.authService.getHttpHeaders(),
            observe: 'response' as 'body',
            responseType: 'blob' as 'json'
        });
    }

    downloadInterestAnalysisCsv(interestPdfAnalysisRequest: InterestPdfAnalysisRequest): Observable<HttpResponse<Blob>> {
        return this.http.post<HttpResponse<Blob>>(Api.ANALYSIS_INTEREST_CSV, interestPdfAnalysisRequest, {
            headers: this.authService.getHttpHeaders(),
            observe: 'response' as 'body',
            responseType: 'blob' as 'json'
        });
    }
}