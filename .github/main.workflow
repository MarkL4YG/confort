workflow "Dummy Main workflow" {
  resolves = ["GitHub Action for Slack"]
  on = "pull_request"
}

action "GitHub Action for Slack" {
  uses = "Ilshidur/action-slack@b0103b6c976ed4e9f702f6a6b54daac573b697ef"
  secrets = ["SLACK_WEBHOOK"]
}
