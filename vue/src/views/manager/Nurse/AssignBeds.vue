<!--<template>-->
<!--  <div >-->
<!--    <div class="search">-->
<!--      <el-input placeholder="请输入病房名称查询" style="width: 200px" v-model="name"></el-input>-->
<!--      <el-button type="primary" plain style="margin-left: 10px" @click="load1(1)">查询</el-button>-->
<!--      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>-->
<!--    </div>-->

<!--    <div class="table" style="width: 50%;">-->
<!--      <el-table :data="tableData" strip @selection-change="handleSelectionChange">-->
<!--        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>-->
<!--        <el-table-column label="图片">-->
<!--          <template v-slot="scope">-->
<!--            &lt;!&ndash; scope&ndash;&gt;-->
<!--            <div style="display: flex; align-items: center">-->
<!--              <el-image-->
<!--                  style="width: 80px; height: 80px; border-radius: 50%"-->
<!--                  :src="scope.row.img"-->
<!--                  :preview-src-list="[scope.row.img]">-->
<!--              </el-image>-->
<!--            </div>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column prop="name" label="病房名称"></el-table-column>-->
<!--        <el-table-column prop="description" label="病房介绍"></el-table-column>-->
<!--        <el-table-column prop="price" label="价格"></el-table-column>-->
<!--        <el-table-column prop="remain" label="剩余数量"></el-table-column>-->
<!--      </el-table>-->

<!--      <div class="paginat ion">-->
<!--        <el-pagination-->
<!--            background-->
<!--            @current-change="handleCurrentChange"-->
<!--            :current-page="pageNum"-->
<!--            :page-sizes="[5, 10, 20]"-->
<!--            :page-size="pageSize"-->
<!--            layout="total, prev, pager, next"-->
<!--            :total="total">-->
<!--        </el-pagination>-->
<!--      </div>-->
<!--    </div>-->


<!--    <div class="table1" style="width: 50%">-->
<!--      <el-table :data="tableDataRecord" strip @selection-change="handleSelectionChange">-->
<!--        <el-table-column type="selection" align="center"></el-table-column>-->
<!--        <el-table-column prop="id" label="序号" align="center" sortable></el-table-column>-->
<!--        <el-table-column prop="name" label="姓名" align="center"></el-table-column>-->
<!--        <el-table-column prop="doctorName" label="医生姓名" show-overflow-tooltip align="center"></el-table-column>-->
<!--        <el-table-column prop="advice" label="医嘱" show-overflow-tooltip align="center"></el-table-column>-->
<!--        <el-table-column label="操作" align="center" width="180">-->
<!--          <template v-slot="scope">-->
<!--            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--      </el-table>-->
<!--    </div>-->


<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import request from "@/utils/request";-->

<!--export default {-->
<!--  name: "AssignBeds",-->
<!--  data() {-->
<!--    return {-->
<!--      tableData: [],  // 所有的数据-->
<!--      pageNum: 1,   // 当前的页码-->
<!--      pageSize: 10,  // 每页显示的个数-->
<!--      total: 0,-->
<!--      name:null,-->
<!--      tableDataRecord:[],-->
<!--    }-->
<!--  },-->
<!--  created() {-->
<!--    this.load1(1); //查询病房-->
<!--    this.load(); //查询病例-->
<!--  },-->
<!--  methods: {-->
<!--    load(){-->
<!--      request.get("/AssignBeds/search").then(res => {-->
<!--        if(res.code === '200'){-->
<!--          this.tableDataRecord = res.data;-->
<!--        }else{-->

<!--        }-->
<!--      })-->
<!--    },-->
<!--    handleSelectionChange(){-->

<!--    },-->
<!--    load1(pageNum) {  // 分页查询-->
<!--      if (pageNum) this.pageNum = pageNum-->
<!--      this.$request.get('/AssignBeds/selectPage', {-->
<!--        params: {-->
<!--          pageNum: this.pageNum,-->
<!--          pageSize: this.pageSize,-->
<!--          username: this.username,-->
<!--        }-->
<!--      }).then(res => {-->
<!--        this.tableData = res.data?.list-->
<!--        this.total = res.data?.total-->
<!--      })-->
<!--    },-->
<!--    reset() {-->
<!--      this.username = null-->
<!--      this.load(1)-->
<!--    },-->

<!--  }-->
<!--}-->
<!--</script>-->

<!--<style scoped>-->

<!--</style>-->

<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入病房名称查询" style="width: 200px" v-model="name"></el-input>
      <el-button type="primary" plain style="margin-left: 10px" @click="load1(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="tables-container" style="display: flex; justify-content: space-between; width: 100%;">
      <div class="table" style="flex: 1; margin-right: 10px;">
        <el-table :data="tableData" strip @selection-change="handleSelectionChange">
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
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-sizes="[5, 10, 20]"
              :page-size="pageSize"
              layout="total, prev, pager, next"
              :total="total">
          </el-pagination>
        </div>
      </div>

      <div class="table" style="flex: 1;">
        <el-table :data="tableDataRecord" strip @selection-change="handleSelectionChange">
          <el-table-column prop="id" label="序号" align="center" sortable></el-table-column>
          <el-table-column prop="name" label="姓名" align="center"></el-table-column>
          <el-table-column prop="doctorName" label="医生姓名" show-overflow-tooltip align="center"></el-table-column>
          <el-table-column prop="advice" label="医嘱" show-overflow-tooltip align="center"></el-table-column>
          <el-table-column label="操作" align="center" width="180">
            <template v-slot="scope">
              <el-button  type="danger" plain @click="handleAssign(scope.row)">未分配病房</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
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
      name: null,
      tableDataRecord: [],
    };
  },
  created() {
    this.load1(1); // 查询病房
    this.load(); // 查询病例
  },
  methods: {
    load() {
      request.get("/AssignBeds/search").then(res => {
        if (res.code === '200') {
          this.tableDataRecord = res.data;
        }
      });
    },
    handleSelectionChange() {},
    load1(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      this.$request.get('/AssignBeds/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
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
  },
};
</script>

<style scoped>
.tables-container {
  display: flex;
  justify-content: space-between;
}
.table {
  flex: 1; /* Adjust this if needed */
  //margin-right: 0px; /* Space between the two tables */
}
</style>