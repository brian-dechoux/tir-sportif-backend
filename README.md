# Project management
Trello: https://trello.com/b/l07Xpxwj/stories

# Database
## Migration
Liquibase
Use of YML: https://www.liquibase.org/documentation/yaml_format.html

# Swagger
## Code generation

https://github.com/swagger-api/swagger-codegen
`java -jar -Dio.swagger.parser.util.RemoteUrl.trustAll=true swagger-codegen-cli.jar generate -i https://localhost:8443/v2/api-docs -l typescript-fetch -o .`
`java -jar -Dio.swagger.parser.util.RemoteUrl.trustAll=true swagger-codegen-cli.jar generate -i https://localhost:8443/v2/api-docs -l typescript-node -o .`
