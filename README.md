
# AstroCluster

AstroCluster is a framework agnostic clustering model specializing in semantic analysis and clustering of projects, supporting various languages.


## Prerequisites
- Docker Compose (version ≥ 3.8)
- Python3
## Installation and Deployment

- In order to full build and deploy the project as it is, run the following script from the **root** of the project:

```bash
./build.sh
```

This will build and deploy all services with the default configuration (skips no service). To customize the build process, the user can enter any combination of the following flags:
- ```--skip-ui```: Skips the build process of the client UI.
- ```--skip-server```: Skips the build process of the backend server.
- ```--skip-grpc```: Skips the automatic gRPC generation from proto files.
- ```--skip-ac```: Skips the build process of the AstroCluster clustering service.
- ```--skip-db```: Skips the build process of the database.
## API Reference
This is the API reference of AstroCluster backend server component. The exposed APIs provide a way to communicate to the AstroCluster clustering service and database (history of analyses of user, retrieval of calculated interest per code file, etc.).

#### Perform clustering on a Git project

```http
  POST /api/cluster/perform-clustering
```

| Parameter    | Type     | Description                |
| :----------- | :------- | :------------------------- |
| `git_url`    | `string` | **Required**. The Git URL of the project to be clustered. |
| `lang`       | `string` | **Required**. The language the project is written in.     |
| `extensions` | `list(string)` | **Required**. The extensions of file to look for.  |

The `extensions` field is to be treated as a custom list of extensions to look for.

This means that the backend will automatically determine the basic extension/s for the language provided **if and only if** the `extensions` field is **EMPTY**. This is just a way to be more flexible if a user desires a more custom approach.


