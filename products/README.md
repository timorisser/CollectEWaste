![](assets/SchrottPot.jpeg)

# Products Microservice

This is the microservice which stores the products. It is written in [Rust](https://www.rust-lang.org/) and uses the excellent [axum](https://github.com/tokio-rs/axum) framework. It uses [PostgreSQL](https://www.postgresql.org) with the [PostGIS](https://postgis.net/) extension for better geospatial data handling as its database. As Axum only really does routing and parameter extraction, a library for the database connection is required. Most ORMs don't like or support the PostGIS extension, so I chose launchbadge's [sqlx](https://github.com/launchbadge/sqlx).

The service provides a REST API. It is specified in detail as a [OpenAPI](https://www.openapis.org/) specification in the [`products.yaml`](../api_specification/products.yaml) file in the `api_specification` subfolder. The [Swagger Editor](https://editor.swagger.io/) can be used to view the file in a more human-readable way.

## How to run

### Container (preferred)

This repository has a [Github Action](../.github/workflows/cd-products.yml) (Github's CI/CD) which automatically builds a container when changes get committed or merged into the main branch.

You can get the container via this command:

```bash
docker pull ghcr.io/pschadauer-tgm/proj2223_collew_products:nightly
```

This will fail, if you aren't logged into Github's container registry. The [documentation](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry) explains how you can log into the registry, although their explanation might only work with POSIX shells. 

### Building from source

Like with any other Rust project, you can build it with `cargo`.

```
cargo build --release
```

This will build the project. The binary file is located inside the `build` directory.

## Environment variables

`DATABASE_URL`: this environment variable stores all database parameters that the service needs. This is the format of the URL: `postgres://username:password@ip:port/database`.

`ACCOUNTS_URL`: the URL to the [account microservice](../account). In the future, if this variable isn't set it will use a fake/stub implementation, so that this service doesn't require the accounts service when working on api endpoints which don't necessarily need it.

`PORT`: the port on which the server will listen. If it is not set, the server will listen at port 8000.
