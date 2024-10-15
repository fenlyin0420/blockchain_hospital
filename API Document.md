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

## 病历详情页面接收参数
```JSON
{
    "id": 11,
    "name": "王五",
    "doctorId": 3,
    "hospitalId": 14,
    "advice": "多喝热水\n少熬夜",
    "drug": "盐酸左氧氟沙星片 4 一日三次\n阿莫西林胶囊 2 一日两次\n健胃消食片 1 一日三次\n",
    "inhospital": null,
    "jurisdiction": "允许",
    "doctorName": "钱有有",
    "hospitalName": null,
    "number": "1717252211259",
    "signData": "张三+2024-06-02 00:53:17",
    "signResult": "成功",
    "signPubKey": "张天志:MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABKy2e+nmxyCU2aitRlrDIE1OTTcp294vDaOaln4eEZmqM/JXdXg03HWGxGsSLhKok+lnMBvdPsP/QqYXHE8HJMk=, 赵千里:MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABL3z9pqrNQYtQzcJOGUbEXiqOGihGILxYcm6WrYCCjImG+0T6GlEF4fvz16CaiBOlWvpZXVMcj8M7snWLT5Gj+k=, 钱有有:MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABIX9mQbIxIlY+U0jrR5AvnAy8de+c5L9HUG5oI6hrr6Dcl605j4U6mkv3KKD86HSZNfoc7vqQuHEBHbqNMvq5WI=, 王有为:MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABBgSW48pZfKEskoYYiqtJgkVHxLR8rttdXv8J+u5s/IZREg4GQlIcXsBuaDe+Jzo5fLHIm7dtaM2NfXSd9CERr4=, 孙晓红:MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABIMjS4S9+L5+YspkZTv2DAatHfP3nkRkodJWCwtAoSeYVUeepazfFTsUuUFCURumUYDnqYQYxtu8tNgZU9L13Hk=",
    "signKey": "4983312662714231970281092985018459311427665918650628828208923045315219141544,40982219744816111194054117167623036749175195835309143328908225171604461393790,57503448068408977236926275040301450064634531186936856244261271705286412042163,36744483198943720669418387871093802472116064459174441142086946966484849244056,7854625183821364321859336456925885808633594278598511085194977304219908187554,40755311266279753640680912139874021689890193193452006092807893402380945748478"
}
```

## 叫号收到的对象
```JSON
{
    "id": 44,
    "doctorId": 3,
    "userId": 1,
    "time": "2024-10-15",
    "status": "已叫号",
    "doctorName": "钱有有",
    "userName": "张三",
    "hospitalId": 12,
    "hospitalName": "xx大学第二附属医院"
}
```

## caseInfo
```JSON
<span class="field-value">{{ caseInfo.userName }} </span>

<span class="field-value">{{ caseInfo.doctorName }} </span>

<span class="field-value">{{ caseInfo.hospitalName }} </span>

<span class="field-value">{{ caseInfo.departmentName }} </span>

<span class="field-value">{{ caseInfo.time }}</span>

<span class="field-value">{{ caseInfo.inhospital }} </span>
```