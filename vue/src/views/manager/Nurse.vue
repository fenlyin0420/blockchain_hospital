<template>
  <div>
    <div class="search">
      <el-input
        placeholder="请输入账号查询"
        style="width: 200px"
        v-model="username"
      ></el-input>
      <el-button type="primary" plain style="margin-left: 10px" @click="load(1)"
        >查询</el-button
      >
      <el-button type="warning" plain style="margin-left: 10px" @click="reset"
        >重置</el-button
      >
      <el-button type="success" style="margin-left: 10px" plain @click="handleAdd"
        >新增</el-button
      >
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column
          prop="id"
          label="序号"
          width="80"
          align="center"
          sortable
        ></el-table-column>
        <el-table-column label="头像">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image
                style="width: 40px; height: 40px; border-radius: 50%"
                v-if="scope.row.avatar"
                :src="scope.row.avatar"
                :preview-src-list="[scope.row.avatar]"
              ></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column
          prop="description"
          label="护士介绍"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="phone"
          label="电话"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="email"
          label="邮箱"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column prop="role" label="角色"></el-table-column>
        <el-table-column prop="time" label="入职时间"></el-table-column>
        <el-table-column prop="position" label="职位"></el-table-column>
        <el-table-column prop="hospitalName" label="医院"></el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)"
              >编辑</el-button
            >
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="paginat ion">
      <el-pagination
        background
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20]"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total"
      >
      </el-pagination>
    </div>

    <div>
      <el-dialog
        title="管理员"
        :visible.sync="fromVisible"
        width="40%"
        :close-on-click-modal="false"
        destroy-on-close
      >
        <el-form
          :model="form"
          label-width="100px"
          style="padding-right: 50px"
          :rules="rules"
          ref="formRef"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" placeholder="姓名"></el-input>
          </el-form-item>
          <el-form-item label="介绍" prop="description">
            <el-input
              type="textarea"
              :rows="4"
              v-model="form.description"
              placeholder="护士介绍"
            ></el-input>
          </el-form-item>
          <el-form-item label="科室" prop="departmentId">
            <el-select
              v-model="form.departmentId"
              placeholder="请选择科室"
              style="width: 100%"
            >
              <el-option
                v-for="item in departmentData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="入职时间" prop="time">
            <el-date-picker
              style="width: 100%"
              v-model="form.time"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
            >
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
            <el-select v-model="form.hospitalId" placeholder="请选择医院">
              <div v-for="item in hospitalList">
                <el-option :label="item.hospitalName" :value="item.id"></el-option>
              </div>
            </el-select>
          </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="fromVisible = false">取 消</el-button>
          <el-button type="primary" @click="save()">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Nurse",
  data() {
    return {
      tableData: [], // 所有的数据
      pageNum: 1, // 当前的页码
      pageSize: 10, // 每页显示的个数
      total: 0,
      username: null,
      fromVisible: false,
      hospitalList: [],
      form: {},
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      rules: {
        username: [{ required: true, message: "请输入账号", trigger: "blur" }],
      },
      ids: [],
      departmentData: [],
    };
  },
  //页面加载的时候，在created里做一些事情
  created() {
    this.load(1);
    this.load2();
    this.loadDepartment();
  },
  //页面上控件触发的方法
  methods: {
    loadDepartment() {
      let id = this.user.hospitalId;
      this.$request.get("/department/selectByH/" + id).then((res) => {
        if (res.code === "200") {
          this.departmentData = res.data;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    handleAdd() {
      // 新增数据
      this.form = {}; // 新增数据的时候清空数据
      this.form.hospitalId = this.user.hospitalId;
      this.fromVisible = true; // 打开弹窗
    },
    handleEdit(row) {
      // 编辑数据
      this.form = JSON.parse(JSON.stringify(row)); // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true; // 打开弹窗
    },
    save() {
      // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? "/nurse/update" : "/nurse/add",
            method: this.form.id ? "PUT" : "POST",
            data: this.form,
          }).then((res) => {
            if (res.code === "200") {
              // 表示成功保存
              this.$message.success("保存成功");
              this.load(1);
              this.fromVisible = false;
            } else {
              this.$message.error(res.msg); // 弹出错误的信息
            }
          });
        }
      });
    },
    del(id) {
      // 单个删除
      this.$confirm("您确定删除吗？", "确认删除", { type: "warning" })
        .then((response) => {
          this.$request.delete("/nurse/delete/" + id).then((res) => {
            if (res.code === "200") {
              // 表示操作成功
              this.$message.success("操作成功");
              this.load(1);
            } else {
              this.$message.error(res.msg); // 弹出错误的信息
            }
          });
        })
        .catch(() => {});
    },
    handleSelectionChange(rows) {
      // 当前选中的所有的行数据
      this.ids = rows.map((v) => v.id);
    },
    delBatch() {
      // 批量删除
      if (!this.ids.length) {
        this.$message.warning("请选择数据");
        return;
      }
      this.$confirm("您确定批量删除这些数据吗？", "确认删除", { type: "warning" })
        .then((response) => {
          this.$request.delete("/nurse/delete/batch", { data: this.ids }).then((res) => {
            if (res.code === "200") {
              // 表示操作成功
              this.$message.success("操作成功");
              this.load(1);
            } else {
              this.$message.error(res.msg); // 弹出错误的信息
            }
          });
        })
        .catch(() => {});
    },
    load(pageNum) {
      // 分页查询
      if (pageNum) this.pageNum = pageNum;
      this.$request
        .get("/nurse/selectPage", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            username: this.username,
            hospitalId: this.user.hospitalId,
          },
        })
        .then((res) => {
          this.tableData = res.data?.list;
          this.total = res.data?.total;
        });
    },
    load2() {
      this.$request.get("/hospital/selectAll").then((res) => {
        this.hospitalList = res.data;
      });
    },
    reset() {
      this.username = null;
      this.load(1);
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum);
    },
  },
};
</script>

<style></style>
