# kotlin-ms-template

Spring-Boot Kotlin template for new microservices

1) REST and GRPC ready
2) Kafka producer/consumer ready
3) Logs in JSON Format
4) Base ready for audit log events system
5) Open-Tracing propagation ready
6) Metrics/Prometheus enabled
7) Using Vault to retrieve security credentials

# Running Local

```
docker-compose up and
$ ./gradlew clean build bootRun
```

# Debugging Local

```
docker-compose up and
Run the TemplateAplication.kt main in your preferred IDE in debug mode and be happy. You must use profiles for enable some features.
```

# Docker Image Build:

```
$ ./gradlew clean build bootBuildImage -x test
```

# Lint

For linter checks, we're using [ktlint](https://ktlint.github.io/)
alongside with [Ktlint Gradle](https://github.com/jlleitschuh/ktlint-gradle).

- Checking lint

```shell
./gradlew ktlintCheck
```

- Fixing lint on the project

```shell
./gradlew ktlintFormat
```

### Ktlint on IntelliJ IDEA

In case you use IntelliJ IDEA, there's a plugin you can install that will help with daily basis development. To install
it go to `Preferences` -> `Plugins` and search for `ktlint` and install it.

You should also generate IntelliJ IDEA Kotlin style files in the project `.idea/` folder. In order to do it, run the
following on a terminal

```shell
./gradlew ktlintApplyToIdea
```

### Releasing version images:

We are using the github action:
https://github.com/mathieudutour/github-tag-action

Every PULL-REQUEST will run the tests and generate the docker-image how the .github/workflows/pull_request.yaml describes.

To increase and generate a final version docker image, the merge.yaml file was respected.

The change log generated in github respect the:

https://github.com/angular/angular.js/blob/master/DEVELOPERS.md#-git-commit-guidelines

### CI/CD in Dev Environment

All patterns and standards that must be followed are in:

https://contaquanto.atlassian.net/wiki/spaces/EN/pages/2006155315/Tribe+Hylian
