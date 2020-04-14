let c = ./common.dhall

let steps =
      [ c.steps.checkout
      , c.steps.java c.javaVersions.default
      , c.BuildStep.Run
          c.Run::{ name = "Generate YAML files", run = "sbt convertDhall" }
      , c.BuildStep.Run
          c.Run::{
          , name = "Check for differences"
          , run = "[[ \$(git status --porcelain | wc -l) -eq 0 ]]"
          }
      ]

in  { name = "Check Dhall configs"
    , on = [ "push", "pull_request" ]
    , jobs.check = c.baseJob steps
    }
