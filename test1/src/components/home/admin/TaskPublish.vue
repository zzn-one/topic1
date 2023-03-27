<template>
    <el-card class="box-card">
        <div slot="header">
            <el-row>
                <el-col :span="12">
                    <div>选题任务发布</div>
                </el-col>
                <el-col :span="12">
                    <div class="header_btn">
                        <el-button plain type="primary" @click="openAddDialog">发布选题任务</el-button>
                    </div>
                </el-col>
            </el-row>
            <span></span>
        </div>
        <div class="item">
            <el-table :data="taskList" style="width: 100%;margin-bottom:30px" height="311">
                <el-table-column type="index" prop="id" align="center" width="70"></el-table-column>
                <el-table-column prop="name" align="center" label="当前任务名称" width="180">
                </el-table-column>
                <el-table-column prop="year" align="center" label="届数" width="180">
                </el-table-column>
                <el-table-column prop="dictMajor.description" align="center" label="专业">
                </el-table-column>
                <el-table-column prop="startTime" align="center" label="开始时间">
                </el-table-column>
                <el-table-column prop="duration" align="center" label="任务时长（分钟）">
                </el-table-column>
                <el-table-column prop="dictTaskStatus.description" align="center" label="任务状态">
                </el-table-column>
                <el-table-column prop="publishName" align="center" label="任务发布人">
                </el-table-column>
                <el-table-column min-width="100">
                    <template slot-scope="scope">
                        <template v-if="scope.row.status === 0">
                            <el-button plain type="success" @click="openUpdateDialog(scope.row)">修改任务</el-button>
                            <el-button plain type="danger" @click="cancelTask(scope.row)">取消任务</el-button>
                        </template>

                    </template>
                </el-table-column>
            </el-table>
            <el-table :data="historyTaskList" style="width: 100%" height="339">
                <el-table-column type="index" align="center" width="70"></el-table-column>
                <el-table-column prop="name" align="center" label="历史任务名称" width="180">
                </el-table-column>
                <el-table-column prop="year" align="center" label="届数" width="180">
                </el-table-column>
                <el-table-column prop="dictMajor.description" align="center" label="专业">
                </el-table-column>
                <el-table-column prop="startTime" align="center" label="开始时间">
                </el-table-column>
                <el-table-column prop="duration" align="center" label="任务时长（分钟）">
                </el-table-column>
                <el-table-column prop="dictTaskStatus.description" align="center" label="任务状态">
                </el-table-column>
                <el-table-column prop="publishName" align="center" label="任务发布人">
                </el-table-column>
            </el-table>
            <DialogAddTask :visible="dialogVisible" @close="closeAddDialog"></DialogAddTask>
            <DialogUpdateTask :visible="updateDialogVisible" @close="closeUpdateDialog" :id="id"></DialogUpdateTask>
        </div>

    </el-card>
</template>
<script>
import jwt_prase from '@/assets/js/jwtPrase';
import DialogAddTask from '@/components/home/admin/DialogAddTask.vue';
import DialogUpdateTask from '@/components/home/admin/DialogUpdateTask.vue';
export default {
    name: "TaskPublish",
    components: [
        DialogAddTask,
        DialogUpdateTask
    ],
    data() {
        return {
            taskList: [],
            historyTaskList: [],
            dialogVisible: false,
            updateDialogVisible: false,
            id: ''
        };
    },
    methods: {
        //显示新增dialog
        openAddDialog() {
            this.dialogVisible = true;
        },
        closeAddDialog(val) {
            this.dialogVisible = val;
        },
        //显示修改dialog
        openUpdateDialog(row) {
            this.updateDialogVisible = true;
            //把task的id送进去
            this.id = row.id
            console.log(row.id);

        },
        closeUpdateDialog(val) {
            this.updateDialogVisible = val;
        },
        //删除任务
        cancelTask(row) {
            this.$confirm("此操作将永久删除该任务: " + row.name + " , 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                //发送请求，删除row.id 对应的任务
                this.deleteById(row.id)

            }).catch(() => {
                this.$message({
                    type: "info",
                    message: "已取消删除"
                });
            });
        },
        //根据id 取消任务
        async deleteById(id) {
            const resp = await this.$http({
                method: "put",
                url: "/task/" + id,
            })
            if (resp.data.code === 200) {
                this.$message({
                    type: "success",
                    message: resp.data.msg
                });
                this.getTaskList()
            } else {
                this.$message({
                    type: "error",
                    message: resp.data.msg
                });
            }

        },

        //获取任务列表
        async getTaskList() {
            let college = jwt_prase().college
            let year = localStorage.getItem("year")
            //根据college获取任务列表
            const tasks = await this.$http.get('/task/' + college + "/" + year)

            if (tasks.data.code === 200) {

                this.taskList = []
                this.historyTaskList = []

                //处理数据
                tasks.data.data.forEach(task => {
                    task.startTime = new Date(task.startTime).toLocaleString()
                    if (task.status === 0 || task.status === 1) {
                        //未开始任务列表
                        this.taskList.push(task)
                    } else {
                        //历史任务列表
                        this.historyTaskList.push(task)
                    }
                });

            }
        },


    },
    created() {
        this.getTaskList()
    },
    watch: {
        dialogVisible(newVal) {
            if (newVal === false) {
                this.getTaskList()
            }
        },
        updateDialogVisible(newVal) {
            if (newVal === false) {
                this.getTaskList()
            }
        }
    },
    components: { DialogAddTask, DialogUpdateTask }
}
</script>
<style scoped lang="less">
.header_btn {
    float: right;
}

.box-card {
    text-align: left;
    line-height: 24px;
    font-size: 18px;
    height: 818px;
    background-color: rgba(255, 255, 255, 0.9);
}
</style>