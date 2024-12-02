export enum SimilarFilesCriteriaEnum {
    NORMAL = "Normal",
    CLUSTER = "Cluster",
}

export namespace SimilarFilesCriteriaEnum {
    export function entries(): any {
        return Object.entries(SimilarFilesCriteriaEnum)
            .filter(([_, value]) => typeof value === "string") // Filter out functions
            .map(([key, value]) => ({ key, value }));
    }
}