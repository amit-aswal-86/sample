# sample
Each sample done by me in branch.

Sample Project has been created based on the orphan branch to create use below steps :
1) git clone <master_repo>.git
2) cd <master_repo>
3) git checkout --orphan <orphan_branch_name or project_name>
4) git rm -rf .
5) git status #To check which branch we are pointing right now. (It shouldn't be master).
5) git add -A
6) git commit -m "Commit Message"
7) git push origin <orphan_branch_name  or project_name>

# only applicable locally
If you updated .gitignore use below steps to update the change locally :
1) cd <repo_dir>
2) git rm -r --cached .
3) git add -A or git add .
4) git commit -m ".gitignore updated"

For accidentely remove file from project Please follow stackoverflow accepted answer link below :
https://stackoverflow.com/questions/2125710/how-to-revert-a-git-rm-r

Use below video about git to get more idea about git command line :- https://www.youtube.com/watch?v=HVsySz-h9r4