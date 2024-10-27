<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入病历id" style="width: 200px" v-model="status"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData"  stripe>
        <!-- <el-table-column prop="id" label="id" width="80" align="center" sortable></el-table-column> -->
        <!-- <el-table-column prop="number" label="账号" v-if="user.role === 'DOCTOR'" show-overflow-tooltip></el-table-column> -->
        <el-table-column prop="userDate" label="就诊日期" width="150" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="name" label="姓名" v-if="user.role === 'DOCTOR'" width="120" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctorName" label="医生姓名" v-if="user.role === 'USER'" width="150" align="center"></el-table-column>
        <el-table-column prop="hospitalName" label="医院名称" v-if="user.role === 'USER'" width="200" align="center"></el-table-column>
        <el-table-column
            label="病情"
            width="400"
            align="center">
          <template slot-scope="scope">
            <el-input
                type="textarea"
                :readonly="true"
                :autosize="{ minRows: 2, maxRows: 3}"
                placeholder=""
                v-model="scope.row.advice">
            </el-input>
          </template>
        </el-table-column>
        <el-table-column
            label="药物"
            width="300"
            align="center">
          <template slot-scope="scope">
            <el-input
                type="textarea"
                :readonly="true"
                :autosize="{ minRows: 2, maxRows: 3}"
                placeholder=""
                v-model="scope.row.drug">
            </el-input>
          </template>
        </el-table-column>
        <el-table-column prop="inhospital" label="是否住院" width="120" align="center"></el-table-column>
       <!-- <el-table-column prop="jurisdiction" label="权限" v-if="user.role === 'DOCTOR'" width="80" align="center"></el-table-column> -->
        <el-table-column label="详情"  align="center">
          <template v-slot="scope">
            <el-button plain type="primary" size="mini" @click="goToCaseDetails(scope.row)">查看</el-button>
          </template>
        </el-table-column>
       <!-- <el-table-column label="操作"  align="center" v-if="user.role === 'DOCTOR'">
         <template v-slot="scope">
           <el-button plain type="danger" size="mini" @click="update(scope.row)" v-if="user.role !=='USER'">编辑</el-button>
           <el-button plain type="danger" size="mini" @click="del(scope.row.id)">删除</el-button>
         </template>
       </el-table-column> -->
      </el-table>

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

    <el-dialog title="病历编辑" :visible.sync="formVisible" width="60%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="number" label="账号">
          <el-input v-model="form.number" autocomplete="off" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="姓名">
          <el-input v-model="form.name" autocomplete="off" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="就诊日期">
          <el-input v-model="form.userDate" autocomplete="off" placeholder="请输入就诊日期"></el-input>
        </el-form-item>
        <el-form-item prop="doctorName" label="医生姓名">
          <el-input v-model="form.doctorName" autocomplete="off" placeholder="请输入医生姓名"></el-input>
        </el-form-item>
        <el-form-item prop="hospitalName" label="医院名称">
          <el-input v-model="form.hospitalName" autocomplete="off" placeholder="请输入医院名称"></el-input>
        </el-form-item>
        <el-form-item prop="jurisdiction" label="权限">
          <el-input v-model="form.jurisdiction" autocomplete="off" placeholder="请输入权限"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "CaseList",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      status: null,
      formVisible: false,
      form: {
        number: '',
        name: '',
        doctorName: '',
        hospitalName: '',
        jurisdiction: ''
      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        number: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        doctorName: [
          { required: true, message: '请输入医生姓名', trigger: 'blur' }
        ],
        hospitalName: [
          { required: true, message: '请输入医院名称', trigger: 'blur' }
        ],
        jurisdiction: [
          { required: true, message: '请输入权限', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    call(row) {
      let reserveData = { ...row, status: '已叫号' };
      this.$request.put('/caseList/update', reserveData).then(res => {
        if (res.code === '200') {
          this.$message.success('叫号成功')
          this.load(1)
          this.record(row)
        }
      })
    },
    record(row) {
      let data = {
        userId: row.userId,
        doctorId: row.doctorId,
      }
      this.$request.post('/record/add', data).then(res => {
        if (res.code === '200') {
          // this.$message.success('数据同步成功')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {
      this.$confirm('您确定取消挂号吗？这个医生不好挂哦！', '灵魂拷问', {type: "warning"}).then(response => {
        this.$request.delete('/traverse/delete/' + id).then(res => {
          if (res.code === '200') {
            // this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    save() {
      this.$request.post('/caseList/add', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          this.load(1)
          this.formVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd(row) {
      // this.form = {
      //   number: '',
      //   name: '',
      //   doctorName: '',
      //   hospitalName: '',
      //   jurisdiction: ''
      // }
      // this.formVisible = true
      this.$router.push({
        path:"/case",
        params:{
          inf:{}
        }
      })
    },
    update(row){
      this.formVisible = true
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/traverse/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.user.role === 'DOCTOR' ? '' : this.user.name,
          status: this.status,
          //name: this.user.name
        }
      }).then(res => {
        this.tableData = res.data?.list
        for (let i = 0; i < this.tableData.length; i++){
          this.tableData[i].userDate = this.tableData[i].userDate?.split('T')[0]
        }

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
    goToCaseDetails(row) {
      this.$router.push({
        name: 'CaseDetails',
        params: {
          inform:row
        }
      });
    },
  }
}
</script>

<style scoped>

</style>
