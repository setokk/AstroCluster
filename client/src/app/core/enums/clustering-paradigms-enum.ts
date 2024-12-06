export enum ClusteringParadigmsEnum {
    MVC = "MVC",
}

export namespace ClusteringParadigmsEnum {
    export function entries(): any {
        return Object.entries(ClusteringParadigmsEnum)
            .filter(([_, value]) => typeof value === "string") // Filter out functions
            .map(([key, value]) => ({ key, value }));
    }
}