# CaseDetails
## revieveData
```JSON
{
    "id": 41,
    "name": "李四",
    "doctorId": 4,
    "hospitalId": 1,
    "advice": "BBpJXJL5qq9GorRkTN9eK8LBaPXnL437Q0eep6ex6Se1Juu09118lU8WhpVRmJVQMGEs+xgv27fe0ruRIVB6QTOoH9EH6/Y+mFI+YDeMdI4k9dcu3h/0+yS2y3UwvOeuZbpkSNIAiFoNoJbwZnGWCztbfcs=",
    "drug": "BM+XK08ICWz/j5hVsUElGysNdBTGWs8I8JsJVucwt+03hxJaPoPTXXZRiCcvwLLuaG7ESRUUq1QwaqPiYabd57au1c1Dp8FawQzFirX87dr1n5SAKIshg6Xr5k5+FCPAV/Tw6D7YR+BA3vEBovqTFxeFo1whcJNkJw==",
    "inhospital": "否",
    "jurisdiction": "允许",
    "doctorName": "王有为",
    "hospitalName": "xx大学第一附属医院",
    "number": "1730007690604",
    "signData": " ",
    "signResult": " ",
    "signPubKey": " ",
    "signKey": " ",
    "userDate": "2024-10-26"
}
```
# caseList
## tableData
```JSON
{
    "id": 20,
    "name": "李四",
    "doctorId": 2,
    "hospitalId": 1,
    "advice": "BP92G7XajIq8SOOdz41bD/zGwXqgCIlFQHMFSyOzHLtVoSBEnTf0Y6GNDWb9iU8hEtSXg8I/6Fp/6y3F2sD9gIXKZDNIUqz71+bSEbsEQos0JZogZxMq6QJKa3syuXsEZHDhY+JTEatifdUDuA==",
    "drug": "BBga9/pPpLYY3kO6Tv8Lo71AhJ/FS2bajN8q1vKRdgBal7opUsmpDtr9qRImPFzN3xTRwS7NqFL0yRmvTmBk3c9yIFA0baY/t1VLOzZp5c/J2HPzIAuWaCvSESqprKeKdSCErMsW8u/vJB7/XdM3agzdTWG+YaYliKykusKNx9S/w3FrRpaC3f4=",
    "inhospital": "未住院",
    "jurisdiction": "允许",
    "doctorName": "赵千里",
    "hospitalName": "xx大学第一附属医院",
    "careStatus": null,
    "wardId": null,
    "wardName": null,
    "number": "1732164945675",
    "signData": " ",
    "signResult": " ",
    "signPubKey": " ",
    "signKey": " ",
    "userDate": "2024-11-21",
    "img": " "
}
```
# case
## caseData
```JSON
{
    "number": 1731666180100,
    "userDate": "2024-11-11",
    "name": "李四",
    "doctorId": 2,
    "hospitalId": 1,
    "advice": "住院开药",
    "drug": "999感冒灵颗粒 2 一日一次\n",
    "inhospital": "否",
    "jurisdiction": "允许",
    "img": " ",
    "signData": " ",
    "signResult": " ",
    "signPubKey": " ",
    "signKey": " ",
    "hospitalName": "xx大学第一附属医院",
    "doctorName": "赵千里"
}
```

# referralRecord
## row
```JSON
{
    "id": 27,
    "userId": 2,
    "outHospitalId": 1,
    "inHospitalId": 2,
    "outDoctorId": 2,
    "inDoctorId": null,
    "outTime": "2024-11-21 13:50:33",
    "inTime": null,
    "why": "我要转诊了",
    "result": "待审批",
    "userName": "李四",
    "outHospitalName": "xx大学第一附属医院",
    "inHospitalName": "xx大学第二附属医院",
    "outDoctorName": "赵千里",
    "inDoctorName": null
}
```

# this.user
```JSON
{
    "id": 1,
    "username": "admin11",
    "name": "管理员A",
    "password": "123456",
    "role": "ADMIN",
    "newPassword": null,
    "avatar": "http://localhost:8090/files/1728913230123-adminA.jpg",
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxLUFETUlOIiwiZXhwIjoxNzMyMTc2OTk4fQ.eq7in9nYqaCc2aYNMeX3FF8miAVQZpTuh1WpXt4QHuI",
    "publicKey": "MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABKNHbtoxR9iSAet7FnTCfXPL/t/jHxAgMu+LtjBYdLM8BZ7D4rTe8cfpjtRBlJSXFzQNCI6es+lSgxq+h9j5dX8=",
    "privateKey": "MIICSwIBADCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBBIIBVTCCAVECAQEEIFWTdau37n66/sTwQXfsdmcayc2tQIOCpyjD9MVbhUq1oIHjMIHgAgEBMCwGByqGSM49AQECIQD////+/////////////////////wAAAAD//////////zBEBCD////+/////////////////////wAAAAD//////////AQgKOn6np2fXjRNWp5Lz2UJp/OXifUVq4+S3by9QU2UDpMEQQQyxK4sHxmBGV+ZBEZqOcmUj+MLv/JmC+FxWkWJM0x0x7w3NqL09necWb3O42tpIVPQqYd8xipHQALfMuUhOfCgAiEA/////v///////////////3ID32shxgUrU7v0CTnVQSMCAQGhRANCAASjR27aMUfYkgHrexZ0wn1zy/7f4x8QIDLvi7YwWHSzPAWew+K03vHH6Y7UQZSUlxc0DQiOnrPpUoMavofY+XV/",
    "hospitalId": 1,
    "hospitalName": null,
    "phone": "13677889922",
    "email": "admin11@xm.com"
}
```

# referralApplication
## caseInfo
```JSON
{
    "id": 34,
    "userId": 2,
    "doctorId": 2,
    "time": "2024-11-21",
    "medicalRecord": null,
    "inhospital": null,
    "inhostpitalRecord": "否",
    "userName": "李四",
    "doctorName": "赵千里",
    "hospitalId": 1,
    "hospitalName": "xx大学第一附属医院",
    "departmentName": "外科"
}
```