workflow "Notify on vulnerability" {
  on = "repository_vulnerability_alert"
  resolves = ["GitHub Action for Slack"]
}

action "GitHub Action for Slack" {
  uses = "Ilshidur/action-slack@master"
  secrets = ["SLACK_WEBHOOK"]
  args = "A new security vulnerability alert has been published relating to confort: https://github.com/MarkL4YG/confort/network/alerts"
}
