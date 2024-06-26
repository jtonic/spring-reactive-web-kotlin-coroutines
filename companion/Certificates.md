# Certificates

## Self-signed certificates

- [x] Generate server private key, cert request, self-signed certificate (signature)

- [x] Spring Web Configuration (server side certificates)

- [ ] IJ HTTP client certificates configuration

  - [ ] Import server public key into client-trust certificates (*.p12)

### mTLS HTTPs calls

- [ ] Generate client private key, cert request, self-signed certificate (signature)

- [ ] Spring Web Configuration (server side certificates). Import client public key into the server-trust.p12

- [ ] IJ HTTP client configuration

    - [ ] Import server public key into client-trust certificates (*.p12)

### Files involved

**Server side:**
  - server.key (pwd: )
  - server.crt (pwd: )
  - server-publickey.pem
  - server-trust.crt - for mTLS
  - server.p12 (pwd: server-p12-pwd)

**Client side:**
  - client.key
  - client.crt
  - client-trust.crt
  - client.p12 (pwd: client-p12-pwd) - for mTLS (contains client.crt and client.key)

## Commands:

### non mTLS HTTPs calls

1. Generate key and certificates

    > openssl req -x509 -nodes -newkey rsa:2048 -keyout server.key -out server.crt -days 730 
    
    - with subject as command args

    > openssl req -x509 -nodes -newkey rsa:2048 -keyout server.key -out server.crt -days 730 -subj "/C=RO/ST=Bucharest/L=Bucharest/O=Ktonic/CN=ktonic.com"
    
    - with subject and san as command args

    > openssl req -x509 -nodes -newkey rsa:2048 -keyout server.key -out server.crt -days 730 -subj "/C=RO/ST=Bucharest/L=Bucharest/O=Ktonic/CN=ktonic.com" -addext "subjectAltName = DNS:ktonic.com,DNS:localhost"
   
2. See the content of the certificate, public and private keys

    >    openssl x509 -in server.crt -text -noout                     # server certificate
    
    >    openssl x509 -in client-trust.crt -text -noout               # client trust certificate
   
    >    openssl rsa -in server.key -text -noout                      # private key
    
    >    openssl rsa -in server-publickey.pem -pubin -text -noout     # server public key
   
    >    openssl pkcs12 -info -in server.p12                          # p12 file content

3. Export the server certificate

    > openssl x509 -in server.crt -pubkey -noout > server-publickey.pem
   
4. Import the server certificate and public key into client-trust.crt

    > cat server.crt server-publickey.pem > client-trust.crt  

5. Prepare a p12 format file containing both server.crt and server.key to be used by spring web ssl configuration

    > openssl pkcs12 -export -out server.p12 -inkey server.key -in server.crt 

6. Test the application with cURL

   > brew install jq
   
   > curl --cacert client-trust.crt https://localhost:8443/customers | jq


## Self-signed CA certificates

### non mTLS HTTPs calls

### mTLS HTTPs calls
