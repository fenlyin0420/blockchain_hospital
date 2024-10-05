## 用户登录
- URL: `/login`
- METHOD： `POST`
- 请求参数：
```
{
    "username": "adminA",
    "password": "123456",
    "role": "ADMIN"
}
```
- 响应参数：
```JSON
{
    "code": "200",
    "msg": "成功",
    "data": {
        "id": 1,
        "username": "adminA",
        "name": "管理员A",
        "password": "123456",
        "role": "ADMIN",
        "newPassword": null,
        "avatar": "http://localhost:8090/files/1697438073596-avatar.png",
        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxLUFETUlOIiwiZXhwIjoxNzI4MTQ2MjI5fQ.l2SqjUmG5hDDBL6KYYhZ4Xwl7QiuWaNFruVJz_xjEHw",
        "publicKey": "11111",
        "privateKey": "11111",
        "hospitalId": 12,
        "hospitalName": null,
        "phone": "13677889922",
        "email": "admin@xm.com"
    }
}
```
