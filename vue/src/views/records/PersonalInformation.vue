<template>
  <div>
    <div class="form-container">
      <div class="left-form">
        <div class="left-content">
          <div style="text-align: center">
            <div class="avatar-container">
              <img :src="dataP.avatar" class="avatar" />
            </div>
            <div class="info-item">姓名: {{dataP.name}}</div>
          </div>
          <div style="padding-top: 10px">
            <el-divider></el-divider>
          </div>
          <div>
            <div class="info-item">年龄: {{dataP.age}}</div>
            <div class="info-item">性别: {{dataP.gender}}</div>
            <div class="info-item">身份证号: {{dataP.idNumber}}</div>
            <div class="info-item">电话号码: {{dataP.phone}}</div>
            <div class="info-item">病历号: {{dataP.medicalRecordNumber}}</div>
          </div>
        </div>
      </div>

      <div class="right-form">
        <div class="right-content">
          <div class="section-title">
            个人密钥
          </div>
          <el-row>
            <el-col :span="12" class="key-col">
              <el-card class="box-card key-box">
                <div slot="header" class="clearfix">
                  <span>个人公钥</span>
                </div>
                <div class="el-textarea el-input--small">
                  <textarea autocomplete="off" class="el-textarea__inner key-textarea">
                    {{dataP.publicKey}}
                  </textarea>
                </div>
              </el-card>
            </el-col>
            <el-col :span="12" class="key-col">
              <el-card class="box-card key-box">
                <div slot="header" class="clearfix">
                  <span>个人私钥</span>
                </div>
                <div class="el-textarea el-input--small">
                  <textarea autocomplete="off" class="el-textarea__inner key-textarea">
                    {{dataP.privateKey}}
                  </textarea>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <div style="text-align: center; margin-top: 20px;">
            <el-button type="primary" class="custom-button" @click="changeKey">修改密钥</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "UserInformation",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      infA: [],
      infB: [],
      dataP: {}
    }
  },
  created() {
    this.searchA();
     // this.searchB();
     // this.searchPerson();
  },
  methods: {
    searchA() {
      request.post("/keys/searchById", this.user).then(res => {
        if (res.code === '200') {
          this.dataP = res.data
        } else {
          this.$message({
            message: res.msg,
            type: 'error'
          })
        }
      })
    },

    changeKey() {
      // Implement logic to change key here
      this.$request.put('/keys/updateKey', this.user).then(res => {
        if (res.code === '200') {
          // 成功更新
          this.searchA()
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
.form-container {
  display: flex;
  justify-content: space-between;
  padding: 20px;
  background-color: #f4f7f9;
}

.left-form, .right-form {
  flex: 1;
}

.left-form {
  margin-right: 20px;
}

.left-content, .right-content {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.avatar-container {
  display: flex;
  justify-content: center;
  align-items: center;
  border: 2px solid #000000;
  border-radius: 50%;
  width: 150px;
  height: 150px;
  margin: 0 auto;
  overflow: hidden;
}

.avatar {
  width: 100%;
  height: 100%;
}

.info-item {
  padding: 10px 30px;
  font-size: 18px;
  color: #333333;
}

.section-title {
  margin: 20px;
  font-size: 25px;
  color: #050505;
  background-color: rgba(169, 169, 169, 0.3);
  height: 40px;
  line-height: 40px;
  text-align: center;
  border-radius: 5px;
}

.key-col {
  padding: 20px;
}

.box-card {
  height: 280px;
  background-color: #e0e0e0;
  color: #333333;
  border-radius: 10px;
}

.key-textarea {
  min-height: 150px;
  height: 180px;
}

.el-textarea__inner {
  display: block;
  resize: vertical;
  padding: 5px 15px;
  line-height: 1.5;
  box-sizing: border-box;
  width: 100%;
  font-size: inherit;
  color: #000000;
  background-color: #f5f5f5;
  border: 1px solid #bdbdbd;
  border-radius: 4px;
}

.el-button {
  width: 80%;
  color: #ffffff;
  background-color: #bdbdbd;
  text-align: center;
  height: 50px;
  font-size: 20px;
  border-color: #6179ad;
}

.el-button--primary {
  background-color: #6179ad;
}

.inf {
  height: 85vh;
  overflow: hidden;
  background-size: 100%;
  align-items: center;
  justify-content: center;
}

.infion {
  height: 85vh;
  overflow: hidden;
  background-size: 100%;
  align-items: center;
  justify-content: center;
}

.key-box .el-card__header {
  text-align: center;
}

.key-box {
  background-color: #e0e0e0;
}

.custom-button {
  width: 200px;
  height: 50px;
  font-size: 20px;
  border-radius: 25px;
}
</style>
