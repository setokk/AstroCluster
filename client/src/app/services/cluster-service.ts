import { HttpClient } from "@angular/common/http";
import { Api } from "./api";
import { Injectable } from "@angular/core";
import {PerformClusteringRequest} from "../core/request/PerformClusteringRequest";
import {PerformClusteringResponse} from "../core/response/PerformClusteringResponse";
import {Observable} from "rxjs";

@Injectable({providedIn: 'root'})
export class ClusterService {
    constructor(private http: HttpClient) {}

    performClustering(requestBody: PerformClusteringRequest): Observable<PerformClusteringResponse> {
        return this.http.post<PerformClusteringResponse>(Api.CLUSTER_PERFORM_CLUSTERING_ENDPOINT, requestBody);
    }
}