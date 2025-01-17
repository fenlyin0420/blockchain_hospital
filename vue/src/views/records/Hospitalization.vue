<template>
    <div>
        <div class="search">
            <el-input placeholder="请输入病历ID" style="width: 200px" v-model="id"></el-input>
            <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
            <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
        </div>

        <div class="table">
            <el-table :data="tableData" stripe>
                <el-table-column prop="treatmentDate" label="就诊日期" width="200" align="center"
                    show-overflow-tooltip></el-table-column>
                <el-table-column prop="userName" label="姓名" v-if="user.role === 'DOCTOR'" width="200" align="center"
                    show-overflow-tooltip></el-table-column>
                <el-table-column prop="doctorName" label="医生姓名" v-if="user.role === 'USER'" width="150"
                    align="center"></el-table-column>
                <el-table-column prop="hospitalName" label="医院名称" v-if="user.role === 'USER'" width="200"
                    align="center"></el-table-column>
                <el-table-column prop="inHospital" label="住院情况" width="200" align="center"></el-table-column>
                <el-table-column label="详情" align="center">
                    <template v-slot="scope">
                        <el-button plain type="primary" size="mini" @click="goToCaseDetails(scope.row)">查看</el-button>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" v-if="user.role === 'DOCTOR'">
                    <template v-slot="scope">
                        <el-button plain type="danger" size="mini" @click="update(scope.row)"
                            v-if="user.role !== 'USER'">开药</el-button>
                    </template>
                </el-table-column>
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
    name: "CaseList",
    data() {
        return {
            tableData: [],
            pageNum: 1,
            pageSize: 10,
            total: 0,
            id: null,
            formVisible: false,
            form: {
                number: '',
                userName: '',
                doctorName: '',
                hospitalName: '',
                jurisdiction: ''
            },
            user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
            currentDate: '', //当前日期
        }
    },
    created() {
        this.load(1);  //获取当前日期
        this.getCurrentDate();
    },
    methods: {
        //获取当前日期
        getCurrentDate() {    
            const date = new Date();
            const year = date.getFullYear().toString(); // 获取年份
            const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 获取月份，加1是因为getMonth()返回的月份是从0开始的
            const day = date.getDate().toString().padStart(2, '0'); // 获取日期
            this.currentDate = `${year}-${month}-${day}`;
        },
        update(row) {
            let caseData = JSON.parse(JSON.stringify(row));
            let caseInfo = {
            userId: caseData?.userId,
            userName: caseData?.userName,
            doctorName: caseData?.doctorName,
            hospitalId: caseData?.hospitalId,
            hospitalName: caseData?.hospitalName,
            departmentName: caseData?.departmentName,
            time: this.currentDate,
          }
            this.$router.push({name: "Case", query: caseInfo})
            this.$message.success('住院开药')
        },
        load(pageNum) {
            if (pageNum) this.pageNum = pageNum
            this.$request.get('/traverse/selectPage', {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    doctorId: this.user.role === 'DOCTOR' ? this.user.id : null,
                    userId: this.user.role === 'USER' ? this.user.id : null,
                    id:this.id, //病历Id，用于查询
                }
            }).then(res => {
                this.tableData = res.data?.list
                this.tableData = this.tableData.filter(item => item.inHospital === "已住院"); 
                this.total = res.data?.total
            })
        },
        reset() {
            this.id = null
            this.load(1)
        },
        handleCurrentChange(pageNum) {
            this.load(pageNum)
        },
        goToCaseDetails(row) {
            this.$router.push({
                name: 'CaseDetail',
                query: row
            });
        },
    }
}
</script>

<style scoped></style>