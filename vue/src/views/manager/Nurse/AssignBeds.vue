<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入病房名称查询" style="width: 200px" v-model="name"></el-input>
      <el-button type="primary" plain style="margin-left: 10px" @click="load1(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="tables-container" style="display: flex; justify-content: space-between; width: 100%;">
      <div class="table" style="flex: 1; margin-right: 10px;">
        <el-table :data="tableData" strip>
          <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
          <el-table-column label="图片">
            <template v-slot="scope">
              <div style="display: flex; align-items: center">
                <el-image
                    style="width: 80px; height: 80px; border-radius: 50%"
                    :src="scope.row.img"
                    :preview-src-list="[scope.row.img]">
                </el-image>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="病房名称"></el-table-column>
          <el-table-column prop="description" label="病房介绍"></el-table-column>
          <el-table-column prop="price" label="价格"></el-table-column>
          <el-table-column prop="remain" label="剩余数量"></el-table-column>
        </el-table>

        <div class="pagination">
          <el-pagination
              background
              :current-page="pageNum"
              :page-sizes="[5, 10, 20]"
              :page-size="pageSize"
              layout="total, prev, pager, next"
              :total="total">
          </el-pagination>
        </div>
      </div>

      <div class="table" style="flex: 1;">
        <el-table :data="tableDataRecord" strip>
          <el-table-column prop="id" label="序号" align="center" sortable></el-table-column>
          <el-table-column prop="name" label="姓名" align="center"></el-table-column>
          <el-table-column prop="doctorName" label="医生姓名" show-overflow-tooltip align="center"></el-table-column>
          <el-table-column prop="advice" label="医嘱" show-overflow-tooltip align="center"></el-table-column>
          <el-table-column prop="wardName" label="病房号" show-overflow-tooltip align="center"></el-table-column>
          <el-table-column label="操作" align="center" width="180">
            <template v-slot="scope">
              <el-button
                  type="primary"
                  plain
                  @click="handleAssign(scope.row)"
                  :disabled="assignedIds.has(scope.row.id)"
              >分配病房</el-button>
            </template>
          </el-table-column>
        </el-table>

<!--        <div class="pagination">-->
<!--          <el-pagination-->
<!--              background-->
<!--              :current-page="pageNum1"-->
<!--              :page-sizes="[5, 10, 20]"-->
<!--              :page-size="pageSize1"-->
<!--              layout="total, prev, pager, next"-->
<!--              :total="total1">-->
<!--          </el-pagination>-->
<!--        </div>-->
      </div>

    </div>

    <!-- dialog -->
    <el-dialog title="分配病床"  :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px">

        <el-form-item label="病房名" prop="wardName">
          <el-select v-model="form.wardId" placeholder="请选择病房">
            <div v-for="item in wardList" :key="item.id">
              <el-option :label="item.name" :value="item.id"></el-option>
            </div>
          </el-select>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "AssignBeds",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      pageNum1:1,
      pageSize1: 10,
      total1: 0,
      name: null,
      tableDataRecord: [],
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      wardList: [],
      assignedIds: new Set(),  // Track assigned IDs
    };
  },
  created() {
    this.load1(1); // 查询病房
    this.load(1); // 查询病例
    this.findWards(); //查询所有的病房信息循环赋值给select下拉框
  },
  methods: {
    findWards() {
      request.get("/ward/findAll").then(res => {
        if (res.code === "200") {
          this.wardList = res.data;
          console.log(this.wardList);
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      this.$request.get('/AssignBeds/selectPage1', {
        params: {
          pageNum1: this.pageNum,
          pageSize1: this.pageSize,
          hospitalId: this.user.hospitalId,
        },
      }).then(res => {
        this.tableDataRecord = res.data?.list;
        this.total1 = res.data?.total;
      });
    },
    load1(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      this.$request.get('/AssignBeds/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        },
      }).then(res => {
        this.tableData = res.data?.list;
        this.total = res.data?.total;
      });
    },
    reset() {
      this.username = null;
      this.load(1);
    },
    handleAssign(row) {   // 新增数据（ward_id == null 的新增一个ward_id）
      this.form = JSON.parse(JSON.stringify(row)); // Clear form and assign row data
      this.assignedIds.add(row.id);  // Add to assigned IDs
      this.form.inhospital = '已住院';
      this.form.careStatus = '未护理';
      this.fromVisible = true;   // 打开弹窗
    },
    save() { // 实际上就是修改操作
      request.put("/AssignBeds/save", this.form).then(res => {
        if (res.code === "200") {
          this.$message.success('保存成功');
          this.load(); // 在此查询渲染病例
          this.fromVisible = false;
        } else {
          this.$message.error(res.msg);
        }
      });
      /** 修改病房表中的剩余挂号数量 plan */
      let wardBody = {
        id : this.form.wardId
      }
      request.post("/ward/updateNum", wardBody).then((res) => {
        if (res.code == "200") {
          this.load1(1);
        }
      });
    },
  }
};
</script>

<style scoped>
.tables-container {
  display: flex;
  justify-content: space-between;
}
.table {
  flex: 1; /* Adjust this if needed */
}
::v-deep .el-input__inner{
  font-family: "SimSun", "宋体", serif;
  font-size: 16px;
  color: blue !important;
}

::v-deep .el-dialog {
    position: relative;
    margin: 0px auto 50px;
    background: #fff;
    border-radius: 20px;
    box-shadow: inset 0 1px 10px rgba(0, 0, 0, .3);
    box-shadow: outset 0 1px 10px rgba(0, 8, 255, 0.3);
    box-sizing: border-box;
    width: 40%;
    margin-left: 57%;
}
</style>