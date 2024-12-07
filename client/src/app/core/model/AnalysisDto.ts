import {ClusterResultDto} from "./ClusterResultDto";

export interface AnalysisDto {
    id: bigint;
    gitProjectName: string;
    gitUrl: string;
    projectUUID: string;
    projectLang: string;
    createdDate: string;
    clusterResults: ClusterResultDto[];
}