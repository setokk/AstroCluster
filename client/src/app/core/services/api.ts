export enum Api {
    URL = "http://localhost:8080/api",
    CLUSTER_ENDPOINT = URL + "/cluster",
    ANALYSIS_ENDPOINT = URL + "/analysis",
    USERS_ENDPOINT = URL + "/users",

    CLUSTER_PERFORM_CLUSTERING_ENDPOINT = CLUSTER_ENDPOINT + "/perform-clustering",
    ANALYSIS_LATEST_ANALYSES_ENDPOINT = ANALYSIS_ENDPOINT + "/latest-analyses",
    ANALYSIS_INTEREST_PDF = ANALYSIS_ENDPOINT + "/interest/pdf",
    USERS_LOGIN_ENDPOINT = USERS_ENDPOINT + "/log-in",
    USERS_REGISTER_ENDPOINT = USERS_ENDPOINT + "/register",
}