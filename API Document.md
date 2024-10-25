# /hospital
## /hospital/selectAll
### 请求
- method: GET
- params: null

### 响应
`hospital[...]`
```
{
    "id": 1,
    "hospitalName": "xx大学第一附属医院",
    "url": "192.168.1.7"
}
```

# /department
## /department/selectAll
### 请求
- method: GET
- params: null

### 响应
`department[...]`
```
{
    "id": 2,
    "name": "外科",
    "description": "这是外科",
    "hospitalId": 1,
    "hospitalName": "xx大学第一附属医院"
}
```

# /doctor
## /doctor/selectpage2
### 请求
- method: GET
- params:
```
{
    pageNum: this.pageNum,
    pageSize: this.pageSize,
    departmentId: this.departmentId,
    hospitalId: this.hospitalId,
    selectedDate: this.selectedDate
}
```

### 响应
```
{
    "total": 2,
    "list": [
        {
            "id": 2,
            "username": "zhao",
            "name": "赵千里",
            "password": "123456",
            "role": "DOCTOR",
            "newPassword": null,
            "avatar": "...",
            "token": null,
            "publicKey": "...",
            "privateKey": "...",
            "hospitalId": 1,
            "hospitalName": "xx大学第一附属医院",
            "phone": "18877776666",
            "email": "zhao@xm.com",
            "description": "...",
            "price": 20,
            "time": "2023-11-07",
            "position": "副主治医师",
            "departmentId": 2,
            "week": null,
            "date": null,
            "period": null,
            "departmentName": "外科",
            "num": 19,
            "selectedDate": "2024-10-25"
        },
        ...
    ],
    "pageNum": 1,
    "pageSize": 10,
    "size": 2,
    "startRow": 1,
    "endRow": 2,
    "pages": 1,
    "prePage": 0,
    "nextPage": 0,
    "isFirstPage": true,
    "isLastPage": true,
    "hasPreviousPage": false,
    "hasNextPage": false,
    "navigatePages": 8,
    "navigatepageNums": [
        1
    ],
    "navigateFirstPage": 1,
    "navigateLastPage": 1
}
```

# /plan
## /plan/selectAll
### 请求
- method: GET
- params: null

### 响应
`plan[...]`
```
{
    "id": 2,
    "name": "外科",
    "description": "这是外科",
    "hospitalId": 1,
    "hospitalName": "xx大学第一附属医院"
}
```

## /plan/updateNum
### 请求
- method: POST
- params:
```
{
    hospitalId: number required
    doctorId: number required
    date: string required
}
```
- description: 更新 plan 表中（医院，医生，日期）对应元组的剩余预约人数

# /reserve
## /reserve/add
### 请求
- method: POST
- params:
```
{
    userId: this.user.id,
    doctorId: item.id,
    hospitalId: item.hospitalId,
    time: item.selectedDate
}
```
- description: 在预约表添加一条记录

## /reserve/selectPage
### 请求
- method: GET
- params:
```
{
    id: number,
    doctorId: number,
    userId: number,
    time: string,
    status: string,
    hospitalId: number,
}
```

### 响应
`reserve[...]`
```
{

    "id": 98,
    "doctorId": 4,
    "userId": 1,
    "time": "2024-10-25",
    "status": "未叫号",
    "hospitalId": 1,
    "doctorName": "王有为",
    "userName": "张三",
    "hospitalName": "xx大学第一附属医院",
    "departmentName": "外科"
}
```

# /user
## /user/update
### 请求
- `method: PUT`
- `params: User entity`
```
{
    id: number **required**,
    username: string,
    password: string,
    name: string,
    phone: string,
    email: string,
    avatar: string,
    role: string,
    hospitalId: number,
    hospitalName: string,
    account: Double,
    publicKey: string,
    privateKey: string,
    ward_id: number
}
```
- description: 根据 id 更新用户信息，如果其他参数都为空，则不更新