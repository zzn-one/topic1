<template>
    <div>
        <el-card class="box-card">
            <div slot="header" class="card_header">
                <el-row>
                    <el-col :span="12">
                        <div class="header_title">
                            <span>学生信息</span>
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
                            <el-form-item label="学号">
                                <el-input v-model="searchForm.sid"></el-input>
                            </el-form-item>
                            <el-form-item label="学生姓名">
                                <el-input v-model="searchForm.sname"></el-input>
                            </el-form-item>
                            <el-form-item label="专业名称">
                                <el-select v-model="searchForm.major">
                                    <el-option v-for="item in majorList" :label="item.description" :value="item.major"
                                        :key="item.major"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" plain class="btns" @click="searchInfo">查询</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </el-row>
                <!-- 表格 -->
                <el-table stripe :data="studentList" border style="width: 100%" height="600px">
                    <el-table-column type="index" :index="indexMethod">
                    </el-table-column>
                    <el-table-column prop="sid" label="学号" min-width="150">
                    </el-table-column>
                    <el-table-column prop="sname" label="学生姓名" min-width="100">
                    </el-table-column>
                    <el-table-column prop="dictMajor.description" label="专业名称" min-width="180">
                    </el-table-column>
                    <el-table-column prop="graduationYear" label="毕业年份" min-width="100">
                    </el-table-column>
                    <el-table-column prop="phone" label="手机号" min-width="150">
                    </el-table-column>
                    <el-table-column prop="email" label="邮箱" min-width="220">
                    </el-table-column>
                    <el-table-column prop="topicName" label="课题名称" min-width="350">
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
            <!-- 毕业生信息导入 -->
            <el-dialog title="毕业生信息导入" :visible.sync="studentMsgImportFormVisible" width="30%" center>
                <input type="file" @change="importStudentMsg" autocomplete="off" />
            </el-dialog>

            <!-- 毕业生信息导出 -->
            <el-dialog title="毕业生信息导出" center :visible.sync="studentMsgExportFormVisible" width=1500px>
                <StudentMsgExportForm></StudentMsgExportForm>                
            </el-dialog>

        </el-card>
        <DialogStuInfo :formType="formType" :sId="sId" :dialog-visible="infoDialogVisible"
            @closeDialog="closeInfoDialog">
        </DialogStuInfo>
    </div>
</template>
<script>
import DialogStuInfo from '@/components/home/admin/DialogStuInfo.vue';
import jwtPrase from '@/assets/js/jwtPrase';
import StudentMsgExportForm from '@/components/home/admin/StudentMsgExportForm.vue';
export default {
    name: "InfoStudent",
    components: {
        DialogStuInfo,
        StudentMsgExportForm
    },
    data() {
        return {
            studentMsgImportFormVisible: false,
            studentMsgExportFormVisible: false,
            infoDialogVisible: false,
            searchForm: {
                sid: '',
                sname: '',
                major: ''
            },
            studentList: [],
            majorList: '',
            formType: '1',
            sId: '',

            // 分页
            total: 100,
            currentPage: 1,
            pageSize: 5,
            pageSizeList: [5, 10, 20, 30]

        }
    },
    methods: {
        //新增按钮
        openInfoAdd() {
            this.formType = '0'
            this.sId = ''
            this.infoDialogVisible = true
        },
        //修改按钮（查看全部信息按钮）
        openInfoUpdate(rowData) {
            this.formType = '1'
            //把sId送进去给DialogStuInfo组件
            this.sId = rowData.sid

            this.infoDialogVisible = true
        },
        // 删除按钮方法
        deleteStuInfo(rowData) {
            //使用rowData里的sId发送删除请求
            this.$confirm("该操作将永久删除用户：" + rowData.sname + " ,是否继续？ ", "永久删除用户", {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
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
        //查询按钮
        searchInfo() {
            this.getStudentList()
        },
        //导入按钮
        importInfo() {
            this.studentMsgImportFormVisible = true
        },
        //毕业生信息 文件上传
        importStudentMsg(e) {
            const file = e.target.files[0]

            let msgBody = '您要上传文件是  :  ' + file.name

            this.$confirm(msgBody, '毕业生信息上传', {
                confirmButtonText: '确认上传',
                cancelButtonText: '取消上传',
                dangerouslyUseHTMLString: true
            }).then(() => {

                //上传到后台
                const formData = new FormData();

                let updated_by = jwtPrase().id
                let updated_time = new Date()

                formData.append("file", file);
                formData.append("updatedBy", updated_by);
                formData.append("updatedTime", updated_time);

                this.$http.post('/userStudent/importMsg', formData, {
                    'Content-type': 'multipart/form-data'
                }).then(res => {
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


                this.studentMsgImportFormVisible = false
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消上传'
                });
                this.studentMsgImportFormVisible = false
            })


        },


        //导出按钮
        exportInfo() {
            this.studentMsgExportFormVisible = true
        },

        closeInfoDialog(val) {
            this.infoDialogVisible = val
        },
        //表的索引
        indexMethod(index) {
            return index + (this.currentPage - 1) * this.pageSize + 1
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
            let college = jwtPrase().college
            const resp = await this.$http.get('/dict/major/college/' + college)
            if (resp.data.code === 200) {
                this.majorList = resp.data.data
            }
        },
        //获取studentList
        async getStudentList() {
            let college = jwtPrase().college
            const resp = await this.$http({
                method: "post",
                url: "/userStudent/" + this.currentPage + "/" + this.pageSize + "/" + college,
                data: this.searchForm
            })
            if (resp.data.code == 200) {
                let page = resp.data.data
                // 分页
                this.total = page.total
                this.studentList = page.records
            }
        },
        //根据主键id删除
        async deleteById(id) {
            const resp = await this.$http({
                method: "delete",
                url: "/userStudent/" + id,
            })

            if (resp.data.code === 200 && resp.data.data === true) {
                this.$message({
                    message: resp.data.msg,
                    type: "success"
                })
                //重新获取毕业生信息列表
                this.getStudentList()
            } else {
                this.$message({
                    message: resp.data.msg,
                    type: "error"
                })
            }

        }

    },
    created() {
        //获取专业列表
        this.getMajorList()
        //发送请求获取所有学生的信息
        this.getStudentList()

    },
    watch: {
        currentPage() {
            this.getStudentList()
        },
        pageSize() {
            this.getStudentList()
        },
        infoDialogVisible(val) {
            if (val === false) {
                this.getStudentList()
            }
        }
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
    line-height: 23px;
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