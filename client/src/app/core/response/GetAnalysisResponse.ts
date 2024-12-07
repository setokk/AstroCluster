import {AnalysisDto} from "../model/AnalysisDto";
import {PercentagePerClusterDto} from "../model/PercentagePerClusterDto";

export interface GetAnalysisResponse {
    analysisData: AnalysisDto;
    percentagesPerCluster: PercentagePerClusterDto[];
}