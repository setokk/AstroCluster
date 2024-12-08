export interface InterestPdfAnalysisRequest {
    analysisId?: bigint;
    similarFilesCriteria?: string;
    isDescriptive: boolean;
    avgPerGenerationLOC?: number;
    perHourLOC?: number;
    perHourSalary?: number;
}