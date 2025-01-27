# Standard Operating Procedure of AGP Ver 1.0
> Doc Auth: 林建克 Jianke LIN | CY Cergy Paris University M1 RS S1
> Ref: NULL 


> [!NOTE] Attention
>
> 1. Deployment actions are only allowed to be performed if they are clearly labeled in the **Deployment Process** below, and any additional actions must be communicated to the document reseller before they are performed; if they affect the continued execution of the solution, then they will be rolled back and agreed to re-specify the checklist, reevaluate, and re-execute.
> 2. immediately after deployment, archive deployment deliverables, including but not limited to: checklists, acceptance cases, and acceptance conclusions.
> 3. possible impacts to the system, including but not limited to the following, if any, communicate in a timely manner, and focus on server performance in full volume
> 4. Migration of servers to availability zones, it is recommended to create VMs with the same name in the new resource group, after the environment is stabilized, delete the old resources and move the new resources to the original resource group


> [!IMPORTANT]
> Always be aware of the current path and have a rollback plan in place
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
begin your dev process  
git add .  // add all the file in current path  
git commit -m "Add sop-doc.md with initial content"  


- Operations relate to Branch
git branch -r | git remote show origin // show all the branch remote  
// git remote show origin will give more infos about relation status and other info  
git pull origin main // if in remote repo, there exits repo inexits local, pull to local and make synchronnization  
git fetch --prune // update to remote repo
git branch --unset-upstream // 不需要当前分支与远程仓库同步，取消当前分支的远程跟踪  
git branch -d main // delete specific branch   
git branch -r // show all the branch local  
git checkout -b master // create master branch for project  
git push origin master // push to repo remote
git branch --set-upstream-to=origin/master master // set up tracking relationships  




- specific upload and update
git add sop-doc.md
git commit -m "Standard Operating Procedure"
git push



