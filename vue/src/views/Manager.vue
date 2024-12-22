<template>
  <div class="manager-container">
    <!--  头部  -->
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" />
        <div class="title">基于SM2可链接环签名的智能区块链转诊系统 V1.0</div>
      </div>

      <!-- 面包屑导航栏 -->
      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <!-- <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{
            $route.meta.name
          }}</el-breadcrumb-item> -->
        </el-breadcrumb>
      </div>

      <div class="manager-header-right my-avatar">
        <el-dropdown placement="bottom">
          <div class="avatar">
            <img
              :src="
                user.avatar ||
                'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
              "
            />
            <div style="color: blue;">{{ user.name || "管理员" }}</div>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="PersonalInformation"
              >密钥管理</el-dropdown-item
            >
            <el-dropdown-item @click.native="goToPerson">个人信息</el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/password')"
              >修改密码</el-dropdown-item
            >
            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!--  主体  -->
    <div class="manager-main">
      <!--  侧边栏  -->
      <div class="manager-main-left">
        <el-menu
          :unique-opened="true"
          router
          style="border: none"
          :default-active="$route.path"
        >
          <!--default-openeds="['info','powerDoctor', 'reserve', 'user','powerUser']"-->
          <!--          <el-menu-item index="/home">-->
          <!--            <i class="el-icon-s-home"></i>-->
          <!--            <span slot="title" style="font-size: 20px;">系统首页</span>-->
          <!--          </el-menu-item>-->


          <!-- 患者模块 -->
          <el-submenu index="userModule" v-if="user.role === 'USER'">
            <template slot="title">
              <!-- <i class="el-icon-menu"></i> -->
              <img src="../assets/imgs/patient.png" alt="" class="el-icon-img">
              <span>患者模块</span>
            </template>
            <el-menu-item index="/doctorCard">预约挂号</el-menu-item>
            <el-menu-item index="/reserve">预约管理</el-menu-item>
            <el-menu-item index="/caseList">溯源病历</el-menu-item>
          </el-submenu>
          <el-submenu  v-else index="userModule" disabled>
            <template slot="title">
              <!-- <i class="el-icon-menu"></i> -->
              <img src="../assets/imgs/patient.png" alt="" class="el-icon-img">
              <span>患者模块</span>
            </template>
            <el-menu-item index="/doctorCard" disabled>预约挂号</el-menu-item>
            <el-menu-item index="/reserve" disabled>预约管理</el-menu-item>
            <el-menu-item index="/caseList" disabled>溯源病历</el-menu-item>
          </el-submenu>

          <!-- 医生模块 -->
            <el-submenu index="doctorModule" v-if="user.role === 'DOCTOR'">
            <template slot="title">
              <!-- <i class="el-icon-menu"></i> -->
              <img src="../assets/imgs/doctor.png" alt="" class="el-icon-img">
              <span>医生模块</span>
            </template>
            <el-menu-item index="/doctorPlan" v-if="user.role === 'DOCTOR'">排班信息</el-menu-item>
            <el-menu-item index="/doctorReserve" v-if="user.role === 'DOCTOR'">当日诊疗</el-menu-item>
            <el-menu-item index="/hospitalization" v-if="user.role === 'DOCTOR'">当日住院</el-menu-item>
            <el-menu-item index="/record" v-if="user.role === 'DOCTOR'">就诊记录</el-menu-item>
            <el-menu-item index="/doctorCaseList" v-if="user.role === 'DOCTOR'">病历列表</el-menu-item>
          </el-submenu>
          <el-submenu v-else index="doctorModule" disabled>
            <template slot="title">
              <!-- <i class="el-icon-menu"></i> -->
              <img src="../assets/imgs/doctor.png" alt="" class="el-icon-img">
              <span>医生模块</span>
            </template>
            <el-menu-item index="/doctorPlan" disabled>医生排班信息</el-menu-item>
            <el-menu-item index="/doctorReserve" disabled>当日诊疗</el-menu-item>
            <el-menu-item index="/hospitalization" disabled>当日住院</el-menu-item>
            <el-menu-item index="/record" disabled>就诊记录</el-menu-item>
            <el-menu-item index="/doctorCaseList" disabled>病历列表</el-menu-item>
          </el-submenu>




          <!-- <el-submenu v-else index="Transfer" disabled>
            <template slot="title">
              <img src="../assets/imgs/referral.png" alt="" class="el-icon-img">
              <span>转诊模块</span>
            </template>
            <el-menu-item index="/referralApplication">转出申请</el-menu-item>
            <el-menu-item index="/referralRecord">转出审批</el-menu-item>
            <el-menu-item index="/SendReferralRecord">发送病历</el-menu-item>
            <el-menu-item>转入审核</el-menu-item>
            <el-menu-item>接收病历</el-menu-item>
            <el-menu-item>转入接诊</el-menu-item>
            <el-menu-item index="/referralRecor">转诊记录</el-menu-item>
          </el-submenu> -->


          <!-- 护士模块 -->
          <el-submenu index="nurseModule" v-if="user.role === 'NURSE'">
            <template slot="title">
              <!-- <i class="el-icon-nurse"></i>  -->
              <img src="../assets/imgs/nurse.png" alt="" class="el-icon-img">
              <span>护士模块</span>
            </template>
            <el-menu-item index="/HealthCheck">健康检测</el-menu-item>
            <el-menu-item index="/AssignBeds">分配床位</el-menu-item>
            <el-menu-item index="/DailyCare">日常护理</el-menu-item>
            <el-menu-item index="/NursePlan">护士排班</el-menu-item>
            <el-menu-item index="/NurseRecord">病床分配记录</el-menu-item>
          </el-submenu>
          <el-submenu v-else index="nurseModule" disabled="" >
            <template slot="title">
              <!-- <i class="el-icon-nurse"></i> -->
              <img src="../assets/imgs/nurse.png" alt="" class="el-icon-img">
              <span>护士模块</span>
            </template>
            <el-menu-item index="/HealthCheck" disabled>健康检测</el-menu-item>
            <el-menu-item index="/AssignBeds" disabled>分配床位</el-menu-item>
            <el-menu-item index="/DailyCare" disabled>日常护理</el-menu-item>
            <el-menu-item index="/NursePlan" disabled>护士排班</el-menu-item>
            <el-menu-item index="/NurseRecord" disabled>病床分配记录</el-menu-item>
          </el-submenu>

          <!-- 转诊模块 -->
          <el-submenu index="Transfer" v-if="user.role === 'ADMIN' || user.role === 'DOCTOR'">
            <template slot="title">
              <img src="../assets/imgs/referral.png" alt="" class="el-icon-img">
              <span>转诊模块</span>
            </template>

            <el-menu-item index="/referralApplication">转出申请</el-menu-item>
            <el-menu-item index="/referralOut">转出审批</el-menu-item>
            <el-menu-item index="/sendTraverse">发送病历</el-menu-item>
            <el-menu-item index="/referralIn">转入审核</el-menu-item>
            <el-menu-item index="/receiveTraverse">接收病历</el-menu-item>
            <el-menu-item index="/receivePatients">转入接诊</el-menu-item>
            <el-menu-item index="/referralRecord">转诊记录</el-menu-item>
          </el-submenu>
          
          <!-- 管理模块 -->
          <el-submenu index="adminModule" v-if="user.role === 'ADMIN'">
            <template slot="title">
              <!-- <i class="el-icon-s-custom"></i> -->
              <img src="../assets/imgs/admin.png" alt="" class="el-icon-img">
              <span>管理模块</span>
            </template>
            <el-menu-item index="/log">系统监控</el-menu-item>
            <el-menu-item index="/notice">公告信息</el-menu-item>
            <el-menu-item index="/hospital">医院信息</el-menu-item>
            <el-menu-item index="/department">科室信息</el-menu-item>
            <el-menu-item index="/drug">药品信息</el-menu-item>
            <el-menu-item index="/doctor">医生管理</el-menu-item>
            <el-menu-item index="/adminDoctorPlan">医生排班</el-menu-item>
            <el-menu-item index="/nurse">护士管理</el-menu-item>
            <el-menu-item index="/nurseplan">护士排班</el-menu-item>
            <el-menu-item index="/user">患者管理</el-menu-item>
            <el-menu-item index="/ward">病房管理</el-menu-item>
            <el-menu-item index="/approval">住院审批</el-menu-item>
          </el-submenu>
          <el-submenu v-else index="adminModule" disabled>
            <template slot="title">
              <!-- <i class="el-icon-s-custom"></i> -->
              <img src="../assets/imgs/admin.png" alt="" class="el-icon-img">
              <span>管理模块</span>
            </template>
            <el-menu-item index="/log" disabled>日志信息</el-menu-item>
            <el-menu-item index="/notice" disabled>公告信息</el-menu-item>
            <el-menu-item index="/hospital" disabled>医院信息</el-menu-item>
            <el-menu-item index="/department" disabled>科室信息</el-menu-item>
            <el-menu-item index="/drug" disabled>药品信息</el-menu-item>
            <el-menu-item index="/doctor" disabled>医生管理</el-menu-item>
            <el-menu-item index="/plan" disabled>医生排班</el-menu-item>
            <el-menu-item index="/nurse" disabled>护士管理</el-menu-item>
            <el-menu-item index="/plan" disabled>护士排班</el-menu-item>
            <el-menu-item index="/user" disabled>患者管理</el-menu-item>
            <el-menu-item index="/ward" disabled>病房管理</el-menu-item>
            <el-menu-item index="/approval" disabled>住院审批</el-menu-item>
          </el-submenu>




        </el-menu>
      </div>

      <!--  数据表格  -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Manager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
    };
  },
  created() {
    if (!this.user.id) {
      this.$router.push("/login");
    }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem("xm-user") || "{}"); // 重新获取下用户的最新信息
    },
    goToPerson() {
      if (this.user.role === "ADMIN") {
        this.$router.push("/adminPerson");
      }
      if (this.user.role === "DOCTOR") {
        this.$router.push("/doctorPerson");
      }
      if (this.user.role === "USER") {
        this.$router.push("/userPerson");
      }
      if (this.user.role === "NURSE") {
        this.$router.push("/nursePerson");
      }
    },
    PersonalInformation() {
      this.$router.push("PersonalInformation");
    },
    logout() {
      localStorage.removeItem("xm-user");
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
/* @import "@/assets/css/manager.css"; */
@import "@/assets/css/newCSS.css"; 

.el-icon-menu span {
  font-size: 30px;
}
::v-deep .my-avatar {
  position: relative;
  right: 80px;
}

.el-icon-s-custom:before {
    content: "\e7ab";
}

.el-icon-img {
  width: 20px; 
  height: 20px;
}
</style>
