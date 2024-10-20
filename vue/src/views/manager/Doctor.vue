<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入账号查询" style="width: 200px" v-model="username"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <!--      <el-button type="danger" plain @click="delBatch">批量删除</el-button> -->
      <el-button type="danger" plain @click="doctorPlan">医生排班</el-button>
    </div>

<!--    <div class="operation">-->
<!--      <el-button type="primary" plain @click="handleAdd">新增</el-button>-->
<!--&lt;!&ndash;      <el-button type="danger" plain @click="delBatch">批量删除</el-button> &ndash;&gt;-->
<!--      <el-button type="danger" plain @click="doctorPlan">医生排班</el-button>-->
<!--    </div>-->

    <div class="table">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="70" align="center" sortable></el-table-column>
        <el-table-column label="头像">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 40px; height: 40px; border-radius: 50%" v-if="scope.row.avatar"
                        :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="账号"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="description" label="医生介绍" show-overflow-tooltip></el-table-column>
        <el-table-column prop="departmentName" label="科室"></el-table-column>
        <el-table-column prop="price" label="挂号费"></el-table-column>
        <el-table-column prop="phone" label="电话" show-overflow-tooltip></el-table-column>
        <el-table-column prop="email" label="邮箱" show-overflow-tooltip></el-table-column>
        <el-table-column prop="role" label="角色"></el-table-column>
        <el-table-column prop="time" label="入职时间"></el-table-column>
        <el-table-column prop="position" label="职位"></el-table-column>
        <el-table-column prop="hospitalName" label="医院"></el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
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


    <el-dialog title="新增医生" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="介绍" prop="description">
          <el-input type="textarea" :rows="4" v-model="form.description" placeholder="医生介绍"></el-input>
        </el-form-item>
        <el-form-item label="科室" ><!--prop="departmentId"-->
          <el-select v-model="form.departmentId" placeholder="请选择科室" style="width: 100%">
            <el-option
                v-for="item in departmentData"
                :key="item.id"
                :label="item.id"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="挂号费" prop="price">
          <el-input v-model="form.price" placeholder="挂号费"></el-input>
        </el-form-item>
        <el-form-item label="入职时间" prop="time">
          <el-date-picker style="width: 100%"
              v-model="form.time"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="职位" prop="grade">
          <el-input v-model="form.position" placeholder="职位"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="医院" prop="hospitalName">
          <el-select v-model="form.hospitalId" placeholder="请选择医院" >
            <div v-for="item in hospitalList">
              <el-option :label="item.hospitalName" :value="item.id" v-if="item.id===user.hospitalId"></el-option>
            </div>
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="编辑医生信息" :visible.sync="fromVisible3" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="介绍" prop="description">
          <el-input type="textarea" :rows="4" v-model="form.description" placeholder="医生介绍"></el-input>
        </el-form-item>
        <el-form-item label="科室" ><!--prop="departmentId"-->
          <el-select v-model="form.departmentId" placeholder="请选择科室" style="width: 100%">
            <el-option
                v-for="item in departmentData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="挂号费" prop="price">
          <el-input v-model="form.price" placeholder="挂号费"></el-input>
        </el-form-item>
        <el-form-item label="入职时间" prop="time">
          <el-date-picker style="width: 100%"
                          v-model="form.time"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="职位" prop="phone">
          <el-input v-model="form.position" placeholder="职位"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="医院" prop="hospitalName">
          <el-select v-model="form.hospitalId" placeholder="请选择医院" >
            <div v-for="item in hospitalList">
              <el-option :label="item.hospitalName" :value="item.id" v-if="item.id===user.hospitalId"></el-option>
            </div>
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible3 = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

      <el-dialog title="统一排班" :visible.sync="fromVisible0" width="40%" :close-on-click-modal="false" destroy-on-close>
        <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
          <el-form-item label="日期" >
            <el-col :span="11" prop="dt">
              <el-date-picker type="date" placeholder="选择日期" v-model="form.date" style="width: 100%;"></el-date-picker>
            </el-col>
            <el-col class="line" :span="2">——</el-col>
            <el-col :span="11" prop="tm">
              <el-select v-model="form.value" placeholder="请选择">
                <el-option
                    v-for="item in times"
                    :value="item">
                </el-option>
              </el-select>
            </el-col>
          </el-form-item><!--
         <el-form-item label="科室" prop="dpName">
            <el-select v-model="form.departmentId" placeholder="请选择">
              <el-option
                  v-for="item in departmentData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="医生" prop="dn">
            <el-select v-model="form.name" placeholder="请选择">
              <el-option
                  v-for="item in tableData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.name">
              </el-option>
            </el-select>
          </el-form-item>-->
        </el-form>

        <template>
          <el-table
              :data="tableData"
              style="width: 120%">

            <el-table-column
                prop="name"
                label="姓名"
                width="180">
            </el-table-column>
            <el-table-column
                prop="departmentName"
                label="科室"
                width="80">
            </el-table-column>
            <el-table-column label="操作" align="center" width="180">
              <template v-slot="scope">
                <el-button size="mini" type="primary" plain @click="flag = !flag">排班</el-button>
                <el-button size="mini" type="danger" plain @click="flag = !flag" dis abled="flag">取消</el-button>
              </template>
            </el-table-column>
          </el-table>
        </template>

      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible0 = false">取 消</el-button>
        <el-button type="primary" @click="ad">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import admin from "@/views/manager/Admin.vue";

export default {
  name: "Doctor",
  computed: {
    admin() {
      return admin
    }
  },
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      flag:true,
      username: null,
      fromVisible: false,
      fromVisible0:false,
      times:['上午','下午','上午和下午'],
      fromVisible3:false,
      hospitalList:[],
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        username: [
          {required: true, message: '请输入账号', trigger: 'blur'},
        ]
      },
      ids: [],
      departmentData: []
    }
  },
  created() {
    this.load(1)
    this.load2()
    this.loadDepartment()
  },
  methods: {
    loadDepartment() {
      let id=this.user.hospitalId
      this.$request.get('/department/selectByH/'+id).then(res => {
        if (res.code === '200') {
          this.departmentData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.form.hospitalId=this.user.hospitalId
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible3 = true   // 打开弹窗
    },
    change() {
      this.flag=!this.flag
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/doctor/update' : '/doctor/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/doctor/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)
    },
    doctorPlan() {
      this.fromVisible0 = true
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/doctor/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/doctor/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          hospitalId:this.user.hospitalId
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
      this.username = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleAvatarSuccess(response, file, fileList) {
      // 把头像属性换成上传的图片的链接
      this.form.avatar = response.data
    },
    ad() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/plan/update' : '/plan/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },

  }
}
</script>

<style scoped>

</style>