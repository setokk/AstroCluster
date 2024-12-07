import { HttpClient } from "@angular/common/http";
import { Api } from "./api";
import { Injectable } from "@angular/core";
import {Observable} from "rxjs";
import {GetAnalysisResponse} from "../response/GetAnalysisResponse";

@Injectable({providedIn: 'root'})
export class AnalysisService {
    constructor(private http: HttpClient) {}

    getAnalysis(analysisId: bigint): Observable<GetAnalysisResponse> {
        return this.http.get<GetAnalysisResponse>(`${Api.ANALYSIS_ENDPOINT}/${analysisId}`);
    }
}