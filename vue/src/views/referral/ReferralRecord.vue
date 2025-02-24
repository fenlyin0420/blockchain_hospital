<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入转诊记录id" style="width: 200px" v-model="status"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="userName" label="患者" width="80px" show-overflow-tooltip></el-table-column>
        <el-table-column prop="reason" label="转诊原因"></el-table-column>
        <el-table-column prop="result" label="结果" width="120px"></el-table-column>
        <el-table-column prop="outHospitalName" label="转出医院" show-overflow-tooltip></el-table-column>
        <el-table-column prop="outTime" label="转出时间" width="120px"></el-table-column>
        <el-table-column prop="inHospitalName" label="转入医院" show-overflow-tooltip></el-table-column>
        <el-table-column prop="inTime" label="转入时间" width="120px"></el-table-column>
        <!-- <el-table-column prop="inTime" label="转入时间" width="120px" :formatter="formatterInTime"></el-table-column> -->
      </el-table>

      <div class="pagination">
        <el-pagination background @current-change="handleCurrentChange" :current-page="pageNum"
          :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ReferralRecord",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      status: null,
      formVisible: false,
      form: {
        out_hospital: '',
        in_hospital: '',
        out_doctor: '',
        in_doctor: '',
        out_time: '',
        in_time: '',
        why: '',
        result: ''
      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        out_hospital: [
          { required: true, message: '请输入转出医院', trigger: 'blur' }
        ],
        in_hospital: [
          { required: true, message: '请输入转入医院', trigger: 'blur' }
        ],
        out_doctor: [
          { required: true, message: '请输入转出医生', trigger: 'blur' }
        ],
        in_doctor: [
          { required: true, message: '请输入转入医生', trigger: 'blur' }
        ],
        out_time: [
          { required: true, message: '请输入转出时间', trigger: 'blur' }
        ],
        in_time: [
          { required: true, message: '请输入转入时间', trigger: 'blur' }
        ],
        why: [
          { required: true, message: '请输入转诊原因', trigger: 'blur' }
        ],
        result: [
          { required: true, message: '请输入结果', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    ok(row) {
      return this.user.role === "ADMIN" && 
      ((row.result === "待审批" && this.user.hospitalId == row.outHospitalId) || (row.result === "待审核" && this.user.hospitalId == row.inHospitalId));
    },
    /**
     * 同意转出 
     * @param row 转诊记录
     */
    update(row) {
      let form = {
        id: row.id,
      }
      this.$request.put('/referal/agreenOut', form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.load(1)
          this.record(row)
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    refuse(row) {
      let form = {
        id: row.id,
      }
      this.$request.put('/referal/refuseOut', form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('拒绝接收')
          this.load(1)
          // this.record(row)
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    record(row) {
      let data = {
        userId: row.userId,
        doctorId: row.inDoctorId,
        hospitalId: this.user.hospitalId
      }
      this.$request.post('/record/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('数据同步成功')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {
      this.$confirm('您确定取消挂号吗？这个医生不好挂哦！', '灵魂拷问', { type: "warning" }).then(response => {
        this.$request.delete('/referralRecord/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => { })
    },
    save() {
      this.$request.post('/referralRecord/add', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          this.load(1)
          this.formVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {
      this.form = {
        out_hospital: '',
        in_hospital: '',
        out_doctor: '',
        in_doctor: '',
        out_time: '',
        in_time: '',
        why: '',
        result: ''
      }
      this.formVisible = true
    },
    load(pageNum) {
      console.log(this.user)
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/referral/selectSuccess', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.status = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    formatterInTime(row, column, cellValue) {
      return cellValue || "等待转入";
    }
  }
}
</script>

<style scoped></style>
