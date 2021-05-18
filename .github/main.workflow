workflow "Pull Request notification" {
  resolves = ["GitHub Action for Slack"]
  on = "pull_request"
}

workflow "Pull Request CI" {
  on = "pull_request"
  resolves = ["Gradle Test & Assemble"]
}

action "Gradle Test & Assemble" {
  uses = "MrRamych/gradle-actions/openjdk-11@2.1"
  args = "test assemble"
}
