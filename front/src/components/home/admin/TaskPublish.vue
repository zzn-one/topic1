<template>
    <el-card class="box-card">
        <div slot="header">
            <el-row>
                <el-col :span="12">
                    <div>选题任务发布</div>
                </el-col>
                <el-col :span="12">
                    <div class="header_btn">
                        <el-button plain type="primary" @click="openDialog">新增选题任务</el-button>
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
                <el-table-column prop="college" align="center" label="学院">
                </el-table-column>
                <el-table-column prop="major" align="center" label="专业">
                </el-table-column>
                <el-table-column prop="startTime" align="center" label="开始时间">
                </el-table-column>
                <el-table-column prop="duration" align="center" label="任务时长（分钟）">
                </el-table-column>
                <el-table-column prop="status" align="center" label="任务状态">
                </el-table-column>
                <el-table-column prop="publishBy" align="center" label="任务发布人">
                </el-table-column>
                <el-table-column>
                    <template slot-scope="scope">
                        <el-button plain type="danger" @click="cancelTask(scope.row)">取消任务</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-table :data="historyTaskList" style="width: 100%" height="339">
                <el-table-column type="index" align="center" width="70"></el-table-column>
                <el-table-column prop="name" align="center" label="历史任务名称" width="180">
                </el-table-column>
                <el-table-column prop="year" align="center" label="届数" width="180">
                </el-table-column>
                <el-table-column prop="college" align="center" label="学院">
                </el-table-column>
                <el-table-column prop="major" align="center" label="专业">
                </el-table-column>
                <el-table-column prop="startTime" align="center" label="开始时间">
                </el-table-column>
                <el-table-column prop="duration" align="center" label="任务时长（分钟）">
                </el-table-column>
                <el-table-column prop="status" align="center" label="任务状态">
                </el-table-column>
                <el-table-column prop="publishBy" align="center" label="任务发布人">
                </el-table-column>
            </el-table>
            <DialogAddTask :visible="dialogVisible" @close="closeDialog"></DialogAddTask>
        </div>

    </el-card>
</template>
<script>
import DialogAddTask from '@/components/home/admin/DialogAddTask.vue';

export default {
    name: "TaskPublish",
    components: [
        DialogAddTask
    ],
    data() {
        return {
            taskList: [],
            historyTaskList: [],
            dialogVisible: false,
        };
    },
    methods: {
        //显示dialog
        openDialog() {
            this.dialogVisible = true;
        },
        closeDialog(val) {
            this.dialogVisible = val;
        },
        //删除任务
        cancelTask(row) {
            console.log(row);
            this.$confirm("此操作将永久删除该任务: " + row.name + " , 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                //发送请求，删除row.id 对应的任务
                this.$message({
                    type: "success",
                    message: "删除成功!"
                });
            }).catch(() => {
                this.$message({
                    type: "info",
                    message: "已取消删除"
                });
            });
        }
    },
    created() {
        //获取任务列表
        this.taskList = [];
        for (let index = 0; index < 150; index++) {
            this.taskList.push({
                id: index,
                name: "任务名称" + index,
                year: 3022,
                college: "xxx学院",
                major: "计算机科学与技术",
                startTime: new Date().toLocaleString(),
                duration: 30,
                status: "未开始",
                publishBy: "江婷"
            });
        }
        //获取历史任务列表
        this.historyTaskList = [];
        for (let index = 0; index < 17; index++) {
            this.historyTaskList.push({
                id: index,
                name: "任务名称" + index,
                year: 3022,
                college: "xxx学院",
                major: "计算机科学与技术",
                startTime: new Date().toLocaleString(),
                duration: 30,
                status: "已完成",
                publishBy: "江婷"
            });
        }
    },
    components: { DialogAddTask }
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