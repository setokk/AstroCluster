import { HttpClient } from "@angular/common/http";
import { Api } from "./api";
import { Injectable } from "@angular/core";

@Injectable({providedIn: 'root'})
export class ClusterService {
    constructor(private http: HttpClient) {}

    performClustering(requestBody: any) {
        this.http.post(Api.PERFORM_CLUSTERING, requestBody).subscribe();
    }
}