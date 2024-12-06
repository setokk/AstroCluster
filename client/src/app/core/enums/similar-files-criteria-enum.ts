export enum SimilarFilesCriteriaEnum {
    Normal = "Normal",
    Cluster = "Cluster",
}

export namespace SimilarFilesCriteriaEnum {
    export function entries(): any {
        return Object.entries(SimilarFilesCriteriaEnum)
            .filter(([_, value]) => typeof value === "string") // Filter out functions
            .map(([key, value]) => ({ key, value }));
    }
}