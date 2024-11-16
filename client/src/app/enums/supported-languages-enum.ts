export enum SupportedLanguagesEnum {
    Java = "java",
}

export namespace SupportedLanguagesEnum {
    export function entries(): any {
        return Object.entries(SupportedLanguagesEnum)
            .filter(([_, value]) => typeof value === "string") // Filter out functions
            .map(([key, value]) => ({ key, value }));
    }
}