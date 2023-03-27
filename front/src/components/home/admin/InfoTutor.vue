<template>
    <div>
        <el-card class="box-card">
            <div slot="header" class="card_header">
                <el-row>
                    <el-col :span="12">
                        <div class="header_title">
                            <span>导师信息</span>
                        </div>
                    </el-col>

                    <el-col :span="12">
                        <div class="header_btns">
                            <el-button class="header_btn btns" plain type="success" @click="importInfo">导出</el-button>
                            <el-button class="header_btn btns" plain type="success" @click="exportInfo">导入</el-button>
                            <el-button class="header_btn btns" plain type="success" @click="openInfoAdd">新增</el-button>
                        </div>
                    </el-col>
                </el-row>

            </div>
            <div class="item">
                <!-- 搜索栏 -->
                <el-row>
                    <div class="header_search">
                        <el-form :inline="true" :model="searchForm" class="demo-form-inline">
                            <el-form-item label="工号">
                                <el-input v-model="searchForm.tId"></el-input>
                            </el-form-item>
                            <el-form-item label="导师姓名">
                                <el-input v-model="searchForm.tName"></el-input>
                            </el-form-item>
                            <el-form-item label="所属系">
                                <el-select v-model="searchForm.major">
                                    <el-option v-for="item in majorList" :label="item.description+'系'"
                                        :value="item.major_id" :key="item.major_id"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" plain class="btns" @click="searchInfo">查询</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </el-row>
                <!-- 表格 -->
                <el-table stripe :data="tutorList" border style="width: 100%" height="600px">
                    <el-table-column type="index">
                    </el-table-column>
                    <el-table-column prop="tId" label="工号" min-width="150">
                    </el-table-column>
                    <el-table-column prop="tName" label="导师姓名" min-width="100">
                    </el-table-column>
                    <el-table-column prop="title" label="导师职称" min-width="100">
                    </el-table-column>
                    <el-table-column prop="major" label="所属系" min-width="180">
                    </el-table-column>
                    <el-table-column prop="phone" label="手机号" min-width="150">
                    </el-table-column>
                    <el-table-column prop="email" label="邮箱" min-width="220">
                    </el-table-column>
                    <el-table-column min-width="250">
                        <template slot-scope="scope">
                            <el-button type="primary" class="btns" plain @click="openInfoUpdate(scope.row)">查看全部信息
                            </el-button>
                            <el-button type="danger" class="btns" plain @click="deleteStuInfo(scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <!-- 分页 -->
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="currentPage" :page-sizes="pageSizeList" :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper" :total="total">
                </el-pagination>
            </div>

        </el-card>
        <DialogTutorInfo ref="dialogInfo" :form-type="formType" :t-id="tId" :dialog-visible="infoDialogVisible"
            @closeDialog="closeInfoDialog">
        </DialogTutorInfo>
    </div>
</template>
<script>
import DialogTutorInfo from '@/components/home/admin/DialogTutorInfo.vue';
export default {
    name: "InfoTutor",
    components: {
        DialogTutorInfo,
    },
    data() {
        return {
            infoDialogVisible: false,
            searchForm: {
                tId: '',
                tName: '',
                major: ''
            },
            tutorList: '',
            majorList: '',
            formType: '1',
            tId: '',

            // 分页
            total: 160,
            currentPage: 2,
            pageSize: 10,
            pageSizeList: [5, 10, 20, 30]

        }
    },
    methods: {
        //新增按钮
        openInfoAdd() {
            this.formType = '0'
            this.tId = ''

            //重置表单
            this.$refs['dialogInfo'].$refs["infoForm"].resetFields()

            this.infoDialogVisible = true
        },
        //修改按钮（查看全部信息按钮）
        openInfoUpdate(rowData) {
            this.formType = '1'
            //把tId送进去给DialogStuInfo组件
            this.tId = rowData.tId

            this.infoDialogVisible = true
        },
        // 删除按钮方法
        deleteStuInfo(rowData) {
            //使用rowData里的tId发送删除请求
            console.log("删除按钮：" + rowData.tId);

        },
        //查询按钮
        searchInfo() {

        },
        //导入按钮
        importInfo() {

        },
        //导出按钮
        exportInfo() {

        }
        ,
        closeInfoDialog(val) {
            this.infoDialogVisible = val
        },
        // 分页的pageSize
        handleSizeChange(val) {
            this.pageSize = val
        },
        // 分页的currentPage
        handleCurrentChange(val) {
            this.currentPage = val
        }

    },
    created() {
        //发送请求获取所有学生的信息
        this.tutorList = []

        // 整点假数据
        for (var i = 1; i <= 20; i++) {
            let id = 303029320200
            this.tutorList.push(
                {
                    tId: id + i + '',
                    tName: "啊啊啊",
                    title:'讲师',
                    major: "计算机科学与技术",
                    graduationYear: "3030",
                    phone: "28038473629",
                    email: "2358472938@pp.com",
                }
            )
        }

        //根据 管理员的所属学院 发送请求 获取专业列表
        this.majorList = [
            { major_id: '0101', description: '计算机科学与技术' },
            { major_id: '0102', description: '软件工程' },
            { major_id: '0103', description: '通信工程' },
            { major_id: '0104', description: '电子商务' },
        ]
    }
}
</script>
<style scoped lang="less">
/deep/.el-dialog__header {
    line-height: 23px;
    font-size: 24px;
}

.header_btn {
    margin-left: 20px;
    float: right;
}

.btns {
    font-size: 16px;
}

.header_search {
    min-width: 1230px;
}

.box-card {
    height: 818px;
    line-height: 23px;
    font-size: 22px;
    text-align: left;
    background-color: rgba(255, 255, 255, 0.9);

}

.header_title {
    line-height: 40px;
}

/deep/.el-form-item__label {

    font-size: 17px;

}

.el-table {
    font-size: 18px;
}
</style>