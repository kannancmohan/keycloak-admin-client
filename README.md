# keycloak-admin-client
sample spring-boot application demonstrating keycloak-admin-client

### Prerequisites
1. java 17
2. keycloak server(see the [installation](#Installing keycloak server) steps below)

### Build the application
```shell
mvn clean install
```

### Start keycloak server
```shell
cd ${KEYCLOAK_HOME}/bin
./kc.sh start-dev --http-port=8083 --log-level=DEBUG
```

### Create a new client in keycloak server(one time setup)
1. Open the Keycloak Admin Console and select your realm
2. Click 'Clients' (left-hand menu) and Click 'Create client'
3. Fill in the form with the following values:
   ```
        Client type: OpenID Connect
        Client ID: keycloak-admin-client-client_id
        Click 'Next'
   ```
4. Enable the option 'Client authentication' and select the 'Service accounts roles' option(all other options are unselected)
5. Click save
6. In the client detail page, select 'Credentials' tab and copy the 'Client secret' and paste it to 'keycloak.credentials.secret' in application.yaml

### Assign appropriate roles to the newly created client(one time setup)
1. In the client detail page , select 'Service accounts roles' tab and click 'Assign role'
2. In the pop-up page select 'Filter by clients' from drop-down 
3. Select the following three roles and click 'Assign'
   ```
        manage-users
        manage-realm
        manage-clients
   ```

### Start keycloak-admin-client
```shell
mvn spring-boot:run
```

## Accessing the endpoints

### get all users
```shell
curl --location --request GET 'http://localhost:8080/api/user'
```

### create new user
```shell
curl --location --request POST 'http://localhost:8080/api/user' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "sample-user",
    "password": "sample-password"
}'
```


## Installing keycloak server
1. Download latest http://www.keycloak.org/downloads.html
2. unzip keycloak-20.0.1.zip
3. cd keycloak-20.0.1/bin
4. start the keycloak server
   ```
    ./kc.sh start-dev
    or
    ./kc.sh start-dev --http-port=8083
    or
    ./kc.sh start-dev --http-port=8083 --log-level=DEBUG
    ```

* for downloading old versions: https://github.com/keycloak/keycloak/releases/download/19.0.1/keycloak-19.0.1.zip

## Additional read 
https://www.keycloak.org/docs/latest/securing_apps/#_spring_boot_adapter
https://www.keycloak.org/docs-api/20.0.2/rest-api/index.html