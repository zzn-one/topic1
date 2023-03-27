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
                            <el-button class="header_btn btns" plain type="success" @click="exportInfo">导出</el-button>
                            <el-button class="header_btn btns" plain type="success" @click="importInfo">导入</el-button>
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
                                <el-input v-model="searchForm.tid"></el-input>
                            </el-form-item>
                            <el-form-item label="导师姓名">
                                <el-input v-model="searchForm.tutorName"></el-input>
                            </el-form-item>
                            <el-form-item label="所属系">
                                <el-select v-model="searchForm.major">
                                    <el-option v-for="item in majorList" :label="item.description + '系'"
                                        :value="item.major" :key="item.major"></el-option>
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
                    <el-table-column type="index" :index="indexMethod" width="80" prop="id">
                    </el-table-column>
                    <el-table-column prop="tid" label="工号" min-width="150">
                    </el-table-column>
                    <el-table-column prop="tutorName" label="导师姓名" min-width="100">
                    </el-table-column>
                    <el-table-column prop="dictTutorTitle.description" label="导师职称" min-width="100">
                    </el-table-column>
                    <el-table-column prop="dictMajor.description" label="所属系" min-width="180">
                    </el-table-column>
                    <el-table-column prop="phone" label="手机号" min-width="150">
                    </el-table-column>
                    <el-table-column prop="email" label="邮箱" min-width="220">
                    </el-table-column>
                    <el-table-column prop="office" label="办公室" min-width="220">
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

            <!-- 指导老师信息导入 -->
            <el-dialog title="指导老师信息导入" :visible.sync="tutorMsgImportFormVisible" width="30%" center>
                <input type="file" @change="importTutorMsg" autocomplete="off" />
            </el-dialog>

            <!-- 指导老师信息导出 -->
            <el-dialog title="指导老师信息导出" center :visible.sync="tutorMsgExportFormVisible" width=1500px>
                <TutorMsgExportForm></TutorMsgExportForm>                
            </el-dialog>

        </el-card>
        <DialogTutorInfo ref="dialogInfo" :form-type="formType" :t-id="tId" :dialog-visible="infoDialogVisible"
            @closeDialog="closeInfoDialog">
        </DialogTutorInfo>
    </div>
</template>
<script>
import jwt_prase from '@/assets/js/jwtPrase';
import DialogTutorInfo from '@/components/home/admin/DialogTutorInfo.vue';
import TutorMsgExportForm from '@/components/home/admin/TutorMsgExportForm.vue';
export default {
    name: "InfoTutor",
    components: {
        DialogTutorInfo,
        TutorMsgExportForm
    },
    data() {
        return {
            tutorMsgImportFormVisible: false,
            tutorMsgExportFormVisible: false,
            infoDialogVisible: false,
            searchForm: {
                tid: null,
                tutorName: null,
                major: null
            },
            tutorList: [],
            majorList: [],
            formType: '1',
            tId: '',

            // 分页
            total: 100,
            currentPage: 1,
            pageSize: 5,
            pageSizeList: [5, 10, 20, 30]

        }
    },
    methods: {
        //表的索引
        indexMethod(index) {
            return index + (this.currentPage - 1) * this.pageSize + 1
        },
        //新增按钮
        openInfoAdd() {
            this.formType = '0'
            this.tId = ''
            this.infoDialogVisible = true
        },
        //修改按钮（查看全部信息按钮）
        openInfoUpdate(rowData) {
            this.formType = '1'
            //把tId送进去给DialogStuInfo组件
            this.tId = rowData.tid

            this.infoDialogVisible = true
        },
        // 删除按钮方法
        deleteStuInfo(rowData) {

            this.$alert("该操作将永久删除用户：" + rowData.tutorName + " ,是否继续？ ", "永久删除用户", {
                type: "warning",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
            }).then(() => {
                //使用rowData里的id发送删除请求
                this.deleteById(rowData.id)

            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        async deleteById(id) {
            const resp = await this.$http({
                method: "delete",
                url: "/userTutor/" + id
            })

            if (resp.data.code === 200) {
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                });
                this.getTutorList()
            }

        },

        //查询按钮
        searchInfo() {
            this.getTutorList()
        },

        //导入按钮
        importInfo() {
            this.tutorMsgImportFormVisible = true
        },
        //指导老师信息 文件上传
        importTutorMsg(e) {
            const file = e.target.files[0]

            let msgBody = '您要上传文件是  :  ' + file.name

            this.$confirm(msgBody, '指导老师信息上传', {
                confirmButtonText: '确认上传',
                cancelButtonText: '取消上传',
                dangerouslyUseHTMLString: true
            }).then(() => {

                //上传到后台
                const formData = new FormData();

                let updated_by = jwt_prase().id
                let updated_time = new Date()

                formData.append("file", file);
                formData.append("updatedBy", updated_by);
                formData.append("updatedTime", updated_time);

                this.$http.post('/userTutor/importMsg', formData, {
                    'Content-type': 'multipart/form-data'
                }).then(res => {

                    console.log(res.data.data);

                    let code = res.data.code
                    if (code === 200) {
                        // 上传成功后的处理
                        this.$message({
                            type: 'success',
                            message: '文件已上传!'
                        });
                    } else if (code === 1001) {
                        this.$message({
                            type: 'error',
                            message: '文件的数据的表头格式错误!'
                        });
                    } else if (code === 1002) {
                        this.$message({
                            type: 'error',
                            message: '文件的数据格式错误!'
                        });
                    } else if (code === 403) {
                        this.$message({
                            type: 'error',
                            message: '文件类型错误或信息重复导入!'
                        });
                    }
                }, err => {
                    // 出现错误时的处理
                    this.$message({
                        type: "error",
                        message: '文件上传失败！请稍后再试。'
                    });
                })


                this.tutorMsgImportFormVisible = false
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消上传'
                });
                this.tutorMsgImportFormVisible = false
            })

        },

        //导出按钮
        exportInfo() {
            this.tutorMsgExportFormVisible = true
        },


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
        },
        //发送请求获取majorList
        async getMajorList() {
            let college = jwt_prase().college
            const resp = await this.$http.get('/dict/major/college/' + college)
            if (resp.data.code === 200) {
                this.majorList = resp.data.data
            }
        },

        //发送请求获取所有导师的信息(根据college 分页 搜索条件（可选）)
        async getTutorList() {
            let college = jwt_prase().college
            const resp = await this.$http({
                method: "post",
                url: "/userTutor/" + this.currentPage + "/" + this.pageSize + "/" + college,
                data: this.searchForm
            })

            if (resp.data.code == 200) {
                let page = resp.data.data
                // 分页
                this.total = page.total

                this.tutorList = page.records

            }
        },
    },
    async created() {
        //获取专业列表
        this.getMajorList()

        //发送请求获取所有导师的信息
        this.getTutorList()

    },
    watch: {
        pageSize() {
            this.getTutorList()
        },
        currentPage() {
            this.getTutorList()
        },
        infoDialogVisible(val) {
            if (val === false) {
                this.getTutorList()
            }
        }

    },

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