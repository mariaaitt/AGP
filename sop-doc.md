# Standard Operating Procedure of AGP Ver 1.0
> Doc Auth: 林建克 Jianke LIN | CY Cergy Paris University M1 RS S1
> Ref: NULL 


> [!NOTE] 注意
>
> 1. 部署动作仅允许执行在下述**部署过程**中明确标注的，一切额外操作均需通过与文档简历者沟通后方可执行；若影响方案继续执行则进行回滚操作，并约定重新指定 Checklist、重新评审、重新实施。
> 2. 部署结束后，立即完成部署交付件归档，包含不限于：checklist、验收 Case、验收结论
> 3. 可能对系统的影响，包括不局限于以下几种，如有，及时沟通，并在全量中关注服务器的性能
> 4. 服务器迁移至可用性区域，建议创建同名 VM 在新的资源组中，在环境稳定后，删除老资源，并将新的资源移动至原有资源组

> [!IMPORTANT]
> 时刻注意当前路径，并备好回滚方案
>

- initialization   
git clone https://github.com/mariaaitt/AGP.git   
cd AGP   
echo "# AGP Project" > README.md  
git add README.md  
git commit -m "Initial commit"  

- create and check branch status  
git checkout -b lin-exp  
git push -u origin lin-exp  
git status  

- create initial file  
echo "# Standard Operating Procedure" > sop-doc.md
