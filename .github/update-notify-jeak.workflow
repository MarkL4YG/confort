workflow "Notify on vulnerability" {
  on = "repository_vulnerability_alert"
  resolves = ["GitHub Action for Slack"]
}

action "GitHub Action for Slack" {
  uses = "Ilshidur/action-slack@master"
  secrets = ["SLACK_WEBHOOK"]
}
