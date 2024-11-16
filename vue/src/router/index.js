import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

//路由分级
const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/home',  // 重定向到主页//需要有前台时必须注册掉，负责就会重定向
    children: [ //二级路由
      { path: '403', name: 'NoAuth', meta: { name: '无权限' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: '管理员信息' }, component: () => import('../views/manager/Admin') },
      { path: 'doctor', name: 'Doctor', meta: { name: '医生信息' }, component: () => import('../views/manager/Doctor') },
      { path: 'nurse', name: 'Nurse', meta: { name: '护士信息' }, component: () => import('../views/manager/Nurse.vue') },
      { path: 'user', name: 'User', meta: { name: '患者信息' }, component: () => import('../views/manager/User') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'doctorPerson', name: 'DoctorPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/DoctorPerson') },
      { path: 'nursePerson', name:'NursePerson', meta: { name: '个人信息' }, component: () => import('../views/manager/nursePerson')},
      { path: 'userPerson', name: 'UserPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/UserPerson') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: '公告信息' }, component: () => import('../views/manager/Notice') },
      { path: 'department', name: 'Department', meta: { name: '科室信息' }, component: () => import('../views/manager/Department') },
      { path: 'adminDoctorPlan', name: 'AdminDoctorPlan', meta: { name: '医生排班' }, component: () => import('../views/manager/Plan') },
      { path: 'doctorPlan', name: 'DoctorPlan', meta: { name: '医生排班' }, component: () => import('../views/manager/Plan') },
      { path: 'doctorCard', name: 'DoctorCard', meta: { name: '预约挂号' }, component: () => import('../views/manager/DoctorCard') },
      { path: 'reserve', name: 'Reserve', meta: { name: '患者挂号' }, component: () => import('../views/manager/Reserve') },
      { path: 'doctorReserve', name: 'DoctorReserve', meta: { name: '当日诊疗' }, component: () => import('../views/manager/Reserve') },
      { path: 'hospitalization', name: 'Hospitalization', meta: { name: '当日住院' }, component: () => import('../views/records/Hospitalization.vue') },
      { path: 'record', name: 'Record', meta: { name: '我的就诊' }, component: () => import('../views/manager/Record') },
      { path: 'registration', name: 'Registration', meta: { name: '住院登记' }, component: () => import('../views/manager/Registration') },
      { path: 'MyFiles', name: 'MyFiles', meta: { name: '我的档案' }, component: () => import('../views/records/MyFiles.vue') },
      { path: 'Power', name: 'Power', meta: { name: '权限管理' }, component: () => import('../views/records/Power.vue') },
      { path: 'log', name: 'Log', meta: { name: '系统监控' }, component: () => import('../views/manager/LogMonitor.vue') },
      { path: 'AddFiles', name: 'AddFiles', meta: { name: '新建档案' }, component: () => import('../views/records/AddFiles.vue') },
      { path: 'SearchFiles', name: 'SearchFiles', meta: { name: '档案检索' }, component: () => import('../views/records/SearchFiles.vue') },
      { path: 'hospital', name: 'hospital', meta: { name: '医院信息' }, component: () => import('../views/records/hospital.vue') },
      { path: 'drug', name: 'Drug', meta: { name: '药品信息' }, component: () => import('../views/records/Drug') },
      { path: 'caseList', name: 'CaseList', meta: { name: '病历列表' }, component: () => import('../views/records/CaseList') },
      { path: 'doctorCaseList', name: 'DoctorCaseList', meta: { name: '病历列表' }, component: () => import('../views/records/CaseList') },
      { path: 'case', name: 'Case', meta: { name: '病历信息' }, component: () => import('../views/records/Case') },
      { path: 'referralRecord', name: 'ReferralRecord', meta: { name: '转诊记录' }, component: () => import('../views/records/ReferralRecord') },
      { path: 'doctorReferralRecord', name: 'DoctorReferralRecord', meta: { name: '转诊记录' }, component: () => import('../views/records/ReferralRecord') },
      { path: 'referralApplication', name: 'ReferralApplication', meta: { name: '转诊申请' }, component: () => import('../views/records/ReferralApplication') },
      { path: 'doctorReferralApplication', name: 'DoctorReferralApplication', meta: { name: '转诊申请' }, component: () => import('../views/records/ReferralApplication') },
      { path: 'caseDetails', name: 'CaseDetails', meta: { name: '病历详情' }, component: () => import('../views/records/CaseDetails.vue') },
      { path: 'PersonalInformation', name: 'PersonalInformation', meta: { name: '密钥管理' }, component: () => import('../views/records/PersonalInformation.vue') },
      // { path: 'nurseplan', name: 'nurseplan', meta:{name: '测试'}, component: () => import('../views/records/ReferralRecord.vue') },
      { path: 'nursePlan', name: 'NursePlan', meta: { name: '护士排班' }, component: () => import('../views/manager/Nurse/NursePlan.vue') },
      { path: 'AssignBeds', name: 'AssignBeds', meta: { name: '分配床位' }, component: () => import('../views/manager/Nurse/AssignBeds.vue') },
      { path: 'DailyCare', name: 'DailyCare', meta: { name: '生活照料' }, component: () => import('../views/manager/Nurse/DailyCare.vue') },
      { path: 'HealthCheck', name: 'HealthCheck', meta: { name: '健康检测' }, component: () => import('../views/manager/Nurse/HealthCheck.vue') },
      { path: 'NurseRecord', name: 'NurseRecord', meta: { name: '病床分配记录' }, component: () => import('../views/manager/Nurse/NurseRecord.vue') },
      { path: 'Ward', name: 'Ward', meta: { name: '病房管理' }, component: () => import('../views/manager/Ward.vue') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/front/Home') },
      { path: 'person', name: 'Person', meta: { name: '个人信息' }, component: () => import('../views/front/Person') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: '注册' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 注：不需要前台的项目，可以注释掉该路由守卫
// 路由守卫
router.beforeEach((to ,from, next) => {
  let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
  if (to.path === '/') {
    if (user.role) {
      if (user.role === 'USER') {
        next('/front/home')
      } else {
        next('/home')
      }
    } else {
      next('/login')
    }
  } else {
    next()
  }
})

export default router
