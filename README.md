# spring-boot-with-axon-framework-demo-01
使用 MySQL &amp; Axon Server 作為範例, 建置基本 DDD 專案

## 啟動需要的外部資源
AxonServer & MySQL  
``` bash
cd docker
docker-compose up -d
```

## Axon
開啟 [Axon server](http://127.0.0.1:8024/)


## 測試
打開 [swagger-ui](http://localhost:8080/swagger-ui/)

## Create Account
Curl
``` bash
curl -X POST "http://localhost:8080/accounts" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"customer\": \"sam\", \"initialBalance\": 1000}"
```

Response body
``` json
{
  "accountNumber": "423376"
}
```

## Deposit Money
Curl
``` bash
curl -X POST "http://localhost:8080/accounts/423376/deposit" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"amount\": 100}"
```

Response body
```
{
  "accountNumber": "423376",
  "balance": 1100,
  "customer": "sam"
}
```

## Withdraw Money
Curl
``` bash
curl -X POST "http://localhost:8080/accounts/423376/withdraw" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"amount\": 500}"
```
	
Response body
``` json
{
  "accountNumber": "423376",
  "balance": 600,
  "customer": "sam"
}
```

if balance < 0  
Response body
``` json
{
  "timestamp": "2021-03-31T15:26:40.7633+08:00",
  "status": 403,
  "error": "Forbidden",
  "message": "An exception was thrown by the remote message handling component: Insufficient Balance: Cannot debit 500 from account number [423376]",
  "path": null
}
```
