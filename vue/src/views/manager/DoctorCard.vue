<template xmlns="">
  <div>
    <!-- 搜索区 -->
    <div class="search">
      <!-- 选择医院 -->
      <el-select placeholder="请选择医院" v-model="hospitalId">
        <el-option
            v-for="item in hospitalList"
            :key="item.id"
            :label="item.hospitalName"
            :value="item.id"></el-option>
      </el-select>

      <!-- 选择科室 -->
      <el-select placeholder="请选择科室" v-model="departmentId">
        <el-option
          v-for="item in planData"
          :key="item.id"
          :label="item.name"
          :value="item.id"></el-option>
      </el-select>



      <div class="searchBtn">
        <el-button type="info" plain style="margin-left: 10px" @click="query(1)">查询</el-button>
        <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      </div>
    </div>

    <!-- doctorCard 显示区域 -->
    <div class="table" style="padding: 15px 20px">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in tableData" style="margin-bottom: 20px">
          <div style="text-align: center; background-color: #ecf8fd" class="card">
            <img :src="item.avatar" alt="" style="width: 100px; height: 100px; border-radius: 50%">
            <div style="font-weight: 550; margin-top: 10px">
              医院 <span style="color: #383535; margin-left: 5px; font-weight: 500">{{item.hospitalName}}</span>
            </div>
            <div style="font-weight: 550; margin-top: 10px">
              {{item.name}} <span style="color: #383535; margin-left: 5px; font-weight: 500">{{item.departmentName}}</span>
            </div>
            <div style="margin-top: 20px; color: #353523; padding: 0 10px; text-align: left; overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 4;">
              简介：{{item.description}}
            </div>
            <div style="margin-top: 15px">
              挂号费：<span style="color: red; font-weight: 550; margin-right: 20px">￥{{item.price}}</span> 剩余：{{item.num}}
            </div>
            <!-- 挂号按钮 -->
            <div style="margin-top: 15px">
              <el-button type="primary" size="mini" :disabled="disabled" @click="showDialog(item)">挂号</el-button>
              <el-dialog
                  title="确认订单"
                  :visible.sync="dialogVisible"
                  width="30%">
                <Payment :form="dialogDate"></Payment>

                <span slot="footer" class="dialog-footer">
                  <el-button @click="dialogVisible = false">Cancel</el-button>
                  <el-button type="primary" @click="reserve(item)">Confirm</el-button>
                </span>
              </el-dialog>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 选择分页区 -->
      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import Payment from "@/views/manager/Payment.vue";
export default {
  components: {
    Payment,
  },
  name: "Doctor",
  data() {
    return {
      tableData: [],  // 所有的数据，经查询得到
      planData: [],
      hospitalList:[],
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      /** 用户选择的医院 */
      hospitalId:null,
      /** 用户选择的科室 */
      departmentId: null,
      fromVisible: false,
      form: {},
      /** user entity, an object */
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {},
      ids: [],
      selectedDate: null,
      disabled: false,
      /** 顶部搜索区域 tab 默认项*/
      activeName: 'hospital',
      /** 支付界面可视性 */
      dialogVisible: false,
      /** 向支付界面传递的数据 */
      dialogDate: {},
    }
  },
  created() {


    this.query(1)
    this.loadPlan()
    this.load2()
  },
  methods: {
    /**
     * 挂号操作
     * 需要修改的表：record.* plan.num user.account
     * @param item 医生信息
     */
    reserve(item) {
      this.dialogVisible = false;
      // this.disabled = true;
      if (this.user.role !== 'USER') {
        this.$message.warning('您的角色不支持挂号操作')
        return
      }
      let reserveBody = {
        userId: this.user.id,
        doctorId: item.id,
        hospitalId: item.hospitalId,
        time: item.selectedDate
      }
      let planBody = {
        hospitalId: item.hospitalId,
        doctorId: item.id,
        date: item.selectedDate,
      }

      /** 修改挂号记录表 */
      this.$request.post('/reserve/add', reserveBody).then(res => {
        if (res.code !== '200') {
          this.$message.error(res.msg)
          return -1
        }
      })
      /** 修改医生排班表中的剩余挂号数量 */
      this.$request.post('/plan/updateNum', planBody).then(res => {
        if (res.code !== '200'){
          this.$message.error("update num failed")
          return -1
        }
      })
      /** 修改账户余额 */
      if (this.user.account < item.price){
        this.$message.error("您的账余额不足")
        return -1
      }
      this.$request.put('/user/update', {account: this.user.account - item.price}).then(res => {
        if (res.code === '200'){
          this.$message.success("挂号成功")
          this.query(1)
        } else {
          this.$message.error(res.data)
        }
      })
    },

    showDialog(item){
      this.dialogVisible = true
      this.dialogDate = {
        department: item.departmentName,
        doctor: item.name,
        price: item.price,
      }
    },

    /**
     * 查询所有计划，其 res.data 是一个数组，数组每一个元素都是一个 plan 表的元组
     * 组件属性 planData 将引用返回的数组（res.data）
     */
    loadPlan() {
      this.$request.get('/department/selectAll').then(res => {
        if (res.code === '200') {
          this.planData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    /**
     * 查询指定页的数据，并将返回数据保存在组件数据中
     * 初始化时默认查询第一页数据
     * res.data 中的 total 是返回的总记录数，被组件属性 total 引用
     * res.data 中的 list 是包含医生个人信息的对象，被组件属性 tableData 引用
     * @param pageNum 页号
     */
    query(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/doctor/selectPage2', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          departmentId: this.departmentId,
          hospitalId:this.hospitalId,
          selectedDate: this.selectedDate
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    load2(){
      this.$request.get('/hospital/selectAll').then(res=>{
        this.hospitalList=res.data
      })
    },
    reset() {
      this.departmentId = null;
      this.hospitalId = null;
      this.selectedDate = null
      this.query(1)
    },
    handleCurrentChange(pageNum) {
      this.query(pageNum)
    },
  }
}
</script>

<style scoped>
.table {
  font-size: 16px;
}

.searchBtn {
  margin: 15px 0;
}
/deep/thead{
  display: none;
}
/deep/.el-card__body{
  cursor: pointer;
}
/deep/.el-calendar__header {
  display: none;
}
</style>