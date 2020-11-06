;; Copyright (c) Cognitect, Inc.
;; All rights reserved.

(ns ^:skip-wiki cognitect.aws.endpoint
  "Impl, don't call directly."
  (:refer-clojure :exclude [resolve])
  (:require [clojure.string :as str]
            [cognitect.aws.util :as util]))

(def endpoints
  {:partitions
   [{:defaults
     {:hostname "{service}.{region}.{dnsSuffix}",
      :protocols ["https"],
      :signatureVersions ["v4"]},
     :dnsSuffix "amazonaws.com",
     :partition "aws",
     :partitionName "AWS Standard",
     :regionRegex "^(us|eu|ap|sa|ca|me|af)\\-\\w+\\-\\d+$",
     :regions
     {:ap-northeast-1 {:description "Asia Pacific (Tokyo)"},
      :ap-northeast-2 {:description "Asia Pacific (Seoul)"},
      :ca-central-1 {:description "Canada (Central)"},
      :us-east-2 {:description "US East (Ohio)"},
      :eu-north-1 {:description "Europe (Stockholm)"},
      :eu-west-2 {:description "Europe (London)"},
      :us-west-1 {:description "US West (N. California)"},
      :af-south-1 {:description "Africa (Cape Town)"},
      :ap-southeast-1 {:description "Asia Pacific (Singapore)"},
      :us-west-2 {:description "US West (Oregon)"},
      :eu-south-1 {:description "Europe (Milan)"},
      :eu-central-1 {:description "Europe (Frankfurt)"},
      :us-east-1 {:description "US East (N. Virginia)"},
      :eu-west-1 {:description "Europe (Ireland)"},
      :eu-west-3 {:description "Europe (Paris)"},
      :ap-east-1 {:description "Asia Pacific (Hong Kong)"},
      :ap-southeast-2 {:description "Asia Pacific (Sydney)"},
      :ap-south-1 {:description "Asia Pacific (Mumbai)"},
      :me-south-1 {:description "Middle East (Bahrain)"},
      :sa-east-1 {:description "South America (Sao Paulo)"}},
     :services
     {:medialive
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "medialive-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "medialive-fips.us-west-2.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "medialive-fips.us-east-2.amazonaws.com"}}},
      :ioteventsdata
      {:endpoints
       {:ap-northeast-1
        {:credentialScope {:region "ap-northeast-1"},
         :hostname "data.iotevents.ap-northeast-1.amazonaws.com"},
        :ap-northeast-2
        {:credentialScope {:region "ap-northeast-2"},
         :hostname "data.iotevents.ap-northeast-2.amazonaws.com"},
        :us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "data.iotevents.us-east-2.amazonaws.com"},
        :eu-west-2
        {:credentialScope {:region "eu-west-2"},
         :hostname "data.iotevents.eu-west-2.amazonaws.com"},
        :ap-southeast-1
        {:credentialScope {:region "ap-southeast-1"},
         :hostname "data.iotevents.ap-southeast-1.amazonaws.com"},
        :us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "data.iotevents.us-west-2.amazonaws.com"},
        :eu-central-1
        {:credentialScope {:region "eu-central-1"},
         :hostname "data.iotevents.eu-central-1.amazonaws.com"},
        :us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "data.iotevents.us-east-1.amazonaws.com"},
        :eu-west-1
        {:credentialScope {:region "eu-west-1"},
         :hostname "data.iotevents.eu-west-1.amazonaws.com"},
        :ap-southeast-2
        {:credentialScope {:region "ap-southeast-2"},
         :hostname "data.iotevents.ap-southeast-2.amazonaws.com"}}},
      :eks
      {:defaults {:protocols ["http" "https"]},
       :endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "fips.eks.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "fips.eks.us-west-2.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "fips.eks.us-east-2.amazonaws.com"}}},
      :budgets
      {:endpoints
       {:aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "budgets.amazonaws.com"}},
       :isRegionalized false,
       :partitionEndpoint "aws-global"},
      :monitoring
      {:defaults {:protocols ["http" "https"]},
       :endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "monitoring-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "monitoring-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "monitoring-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "monitoring-fips.us-east-2.amazonaws.com"}}},
      :comprehend
      {:defaults {:protocols ["https"]},
       :endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "comprehend-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "comprehend-fips.us-west-2.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "comprehend-fips.us-east-2.amazonaws.com"}}},
      :ec2
      {:defaults {:protocols ["http" "https"]},
       :endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "ec2-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "ec2-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "ec2-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "ec2-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "ec2-fips.us-east-2.amazonaws.com"}}},
      :guardduty
      {:defaults {:protocols ["https"]},
       :endpoints
       {:us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "guardduty-fips.us-east-2.amazonaws.com"},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "guardduty-fips.us-west-2.amazonaws.com"},
        :us-west-1-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "guardduty-fips.us-west-1.amazonaws.com"},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "guardduty-fips.us-east-1.amazonaws.com"}},
       :isRegionalized true},
      :elasticloadbalancing
      {:defaults {:protocols ["https"]},
       :endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "elasticloadbalancing-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "elasticloadbalancing-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "elasticloadbalancing-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname
         "elasticloadbalancing-fips.us-east-2.amazonaws.com"}}},
      :groundstation
      {:endpoints
       {:fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "groundstation-fips.us-west-2.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "groundstation-fips.us-east-2.amazonaws.com"}}},
      :cloudformation
      {:endpoints
       {:us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "cloudformation-fips.us-east-2.amazonaws.com"},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "cloudformation-fips.us-west-2.amazonaws.com"},
        :us-west-1-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "cloudformation-fips.us-west-1.amazonaws.com"},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "cloudformation-fips.us-east-1.amazonaws.com"}}},
      :es
      {:endpoints
       {:fips
        {:credentialScope {:region "us-west-1"},
         :hostname "es-fips.us-west-1.amazonaws.com"}}},
      :codepipeline
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "codepipeline-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "codepipeline-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "codepipeline-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "codepipeline-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "codepipeline-fips.us-east-2.amazonaws.com"}}},
      :cloudtrail
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "cloudtrail-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "cloudtrail-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "cloudtrail-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "cloudtrail-fips.us-east-2.amazonaws.com"}}},
      :application-autoscaling {:defaults {:protocols ["http" "https"]}},
      :autoscaling-plans {:defaults {:protocols ["http" "https"]}},
      :waf
      {:endpoints
       {:aws-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "waf-fips.amazonaws.com"},
        :aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "waf.amazonaws.com"}},
       :isRegionalized false,
       :partitionEndpoint "aws-global"},
      :ce
      {:endpoints
       {:aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "ce.us-east-1.amazonaws.com"}},
       :isRegionalized false,
       :partitionEndpoint "aws-global"},
      :dms
      {:endpoints
       {:dms-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "dms-fips.us-west-1.amazonaws.com"}}},
      :workdocs
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "workdocs-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "workdocs-fips.us-west-2.amazonaws.com"}}},
      :glacier
      {:defaults {:protocols ["http" "https"]},
       :endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "glacier-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "glacier-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "glacier-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "glacier-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "glacier-fips.us-east-2.amazonaws.com"}}},
      :sns
      {:defaults {:protocols ["http" "https"]},
       :endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "sns-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "sns-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "sns-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "sns-fips.us-east-2.amazonaws.com"}}},
      :autoscaling {:defaults {:protocols ["http" "https"]}},
      :states
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "states-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "states-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "states-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "states-fips.us-east-2.amazonaws.com"}}},
      :license-manager
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "license-manager-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "license-manager-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "license-manager-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "license-manager-fips.us-east-2.amazonaws.com"}}},
      :oidc
      {:endpoints
       {:ap-northeast-1
        {:credentialScope {:region "ap-northeast-1"},
         :hostname "oidc.ap-northeast-1.amazonaws.com"},
        :ap-northeast-2
        {:credentialScope {:region "ap-northeast-2"},
         :hostname "oidc.ap-northeast-2.amazonaws.com"},
        :ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "oidc.ca-central-1.amazonaws.com"},
        :us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "oidc.us-east-2.amazonaws.com"},
        :eu-north-1
        {:credentialScope {:region "eu-north-1"},
         :hostname "oidc.eu-north-1.amazonaws.com"},
        :eu-west-2
        {:credentialScope {:region "eu-west-2"},
         :hostname "oidc.eu-west-2.amazonaws.com"},
        :ap-southeast-1
        {:credentialScope {:region "ap-southeast-1"},
         :hostname "oidc.ap-southeast-1.amazonaws.com"},
        :us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "oidc.us-west-2.amazonaws.com"},
        :eu-central-1
        {:credentialScope {:region "eu-central-1"},
         :hostname "oidc.eu-central-1.amazonaws.com"},
        :us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "oidc.us-east-1.amazonaws.com"},
        :eu-west-1
        {:credentialScope {:region "eu-west-1"},
         :hostname "oidc.eu-west-1.amazonaws.com"},
        :ap-southeast-2
        {:credentialScope {:region "ap-southeast-2"},
         :hostname "oidc.ap-southeast-2.amazonaws.com"},
        :ap-south-1
        {:credentialScope {:region "ap-south-1"},
         :hostname "oidc.ap-south-1.amazonaws.com"}}},
      :storagegateway
      {:endpoints
       {:fips
        {:credentialScope {:region "ca-central-1"},
         :hostname "storagegateway-fips.ca-central-1.amazonaws.com"}}},
      :glue
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "glue-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "glue-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "glue-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "glue-fips.us-east-2.amazonaws.com"}}},
      :ssm
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "ssm-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "ssm-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "ssm-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "ssm-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "ssm-fips.us-east-2.amazonaws.com"}}},
      :lambda
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "lambda-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "lambda-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "lambda-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "lambda-fips.us-east-2.amazonaws.com"}}},
      :shield
      {:defaults
       {:protocols ["https"],
        :sslCommonName "shield.us-east-1.amazonaws.com"},
       :endpoints
       {:aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "shield.us-east-1.amazonaws.com"},
        :fips-aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "shield-fips.us-east-1.amazonaws.com"}},
       :isRegionalized false,
       :partitionEndpoint "aws-global"},
      :codecommit
      {:endpoints
       {:fips
        {:credentialScope {:region "ca-central-1"},
         :hostname "codecommit-fips.ca-central-1.amazonaws.com"}}},
      :mediaconvert
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "mediaconvert-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "mediaconvert-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "mediaconvert-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "mediaconvert-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "mediaconvert-fips.us-east-2.amazonaws.com"}}},
      :streams.dynamodb
      {:defaults
       {:credentialScope {:service "dynamodb"},
        :protocols ["http" "https"]},
       :endpoints
       {:us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "dynamodb-fips.us-east-2.amazonaws.com"},
        :ca-central-1-fips
        {:credentialScope {:region "ca-central-1"},
         :hostname "dynamodb-fips.ca-central-1.amazonaws.com"},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "dynamodb-fips.us-west-2.amazonaws.com"},
        :us-west-1-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "dynamodb-fips.us-west-1.amazonaws.com"},
        :local
        {:credentialScope {:region "us-east-1"},
         :hostname "localhost:8000",
         :protocols ["http"]},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "dynamodb-fips.us-east-1.amazonaws.com"}}},
      :logs
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "logs-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "logs-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "logs-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "logs-fips.us-east-2.amazonaws.com"}}},
      :inspector
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "inspector-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "inspector-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "inspector-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "inspector-fips.us-east-2.amazonaws.com"}}},
      :transfer
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "transfer-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "transfer-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "transfer-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "transfer-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "transfer-fips.us-east-2.amazonaws.com"}}},
      :elasticmapreduce
      {:defaults
       {:protocols ["https"],
        :sslCommonName "{region}.{service}.{dnsSuffix}"},
       :endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "elasticmapreduce-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "elasticmapreduce-fips.us-west-2.amazonaws.com"},
        :eu-central-1 {:sslCommonName "{service}.{region}.{dnsSuffix}"},
        :us-east-1 {:sslCommonName "{service}.{region}.{dnsSuffix}"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "elasticmapreduce-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "elasticmapreduce-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "elasticmapreduce-fips.us-east-2.amazonaws.com"}}},
      :support
      {:endpoints
       {:aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "support.us-east-1.amazonaws.com"}},
       :partitionEndpoint "aws-global"},
      :runtime.sagemaker
      {:endpoints
       {:us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "runtime-fips.sagemaker.us-east-2.amazonaws.com"},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "runtime-fips.sagemaker.us-west-2.amazonaws.com"},
        :us-west-1-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "runtime-fips.sagemaker.us-west-1.amazonaws.com"},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "runtime-fips.sagemaker.us-east-1.amazonaws.com"}}},
      :events
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "events-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "events-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "events-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "events-fips.us-east-2.amazonaws.com"}}},
      :cloudhsmv2 {:defaults {:credentialScope {:service "cloudhsm"}}},
      :workmail {:defaults {:protocols ["https"]}},
      :api.detective {:defaults {:protocols ["https"]}},
      :rekognition
      {:endpoints
       {:rekognition-fips.us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "rekognition-fips.us-west-1.amazonaws.com"},
        :rekognition-fips.ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "rekognition-fips.ca-central-1.amazonaws.com"},
        :rekognition-fips.us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "rekognition-fips.us-west-2.amazonaws.com"},
        :rekognition-fips.us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "rekognition-fips.us-east-2.amazonaws.com"},
        :rekognition-fips.us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "rekognition-fips.us-east-1.amazonaws.com"}}},
      :sqs
      {:defaults
       {:protocols ["http" "https"],
        :sslCommonName "{region}.queue.{dnsSuffix}"},
       :endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "sqs-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "sqs-fips.us-west-2.amazonaws.com"},
        :us-east-1 {:sslCommonName "queue.{dnsSuffix}"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "sqs-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "sqs-fips.us-east-2.amazonaws.com"}}},
      :iot {:defaults {:credentialScope {:service "execute-api"}}},
      :fms
      {:defaults {:protocols ["https"]},
       :endpoints
       {:fips-sa-east-1
        {:credentialScope {:region "sa-east-1"},
         :hostname "fms-fips.sa-east-1.amazonaws.com"},
        :fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "fms-fips.us-east-1.amazonaws.com"},
        :fips-ap-south-1
        {:credentialScope {:region "ap-south-1"},
         :hostname "fms-fips.ap-south-1.amazonaws.com"},
        :fips-eu-west-3
        {:credentialScope {:region "eu-west-3"},
         :hostname "fms-fips.eu-west-3.amazonaws.com"},
        :fips-ap-southeast-1
        {:credentialScope {:region "ap-southeast-1"},
         :hostname "fms-fips.ap-southeast-1.amazonaws.com"},
        :fips-ap-southeast-2
        {:credentialScope {:region "ap-southeast-2"},
         :hostname "fms-fips.ap-southeast-2.amazonaws.com"},
        :fips-eu-west-2
        {:credentialScope {:region "eu-west-2"},
         :hostname "fms-fips.eu-west-2.amazonaws.com"},
        :fips-ap-northeast-1
        {:credentialScope {:region "ap-northeast-1"},
         :hostname "fms-fips.ap-northeast-1.amazonaws.com"},
        :fips-ap-northeast-2
        {:credentialScope {:region "ap-northeast-2"},
         :hostname "fms-fips.ap-northeast-2.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "fms-fips.us-west-2.amazonaws.com"},
        :fips-eu-central-1
        {:credentialScope {:region "eu-central-1"},
         :hostname "fms-fips.eu-central-1.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "fms-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "fms-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "fms-fips.us-east-2.amazonaws.com"},
        :fips-eu-west-1
        {:credentialScope {:region "eu-west-1"},
         :hostname "fms-fips.eu-west-1.amazonaws.com"}}},
      :mq
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "mq-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "mq-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "mq-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "mq-fips.us-east-2.amazonaws.com"}}},
      :waf-regional
      {:endpoints
       {:ap-northeast-1
        {:credentialScope {:region "ap-northeast-1"},
         :hostname "waf-regional.ap-northeast-1.amazonaws.com"},
        :ap-northeast-2
        {:credentialScope {:region "ap-northeast-2"},
         :hostname "waf-regional.ap-northeast-2.amazonaws.com"},
        :fips-sa-east-1
        {:credentialScope {:region "sa-east-1"},
         :hostname "waf-regional-fips.sa-east-1.amazonaws.com"},
        :fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "waf-regional-fips.us-east-1.amazonaws.com"},
        :ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "waf-regional.ca-central-1.amazonaws.com"},
        :us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "waf-regional.us-east-2.amazonaws.com"},
        :eu-north-1
        {:credentialScope {:region "eu-north-1"},
         :hostname "waf-regional.eu-north-1.amazonaws.com"},
        :fips-me-south-1
        {:credentialScope {:region "me-south-1"},
         :hostname "waf-regional-fips.me-south-1.amazonaws.com"},
        :fips-ap-south-1
        {:credentialScope {:region "ap-south-1"},
         :hostname "waf-regional-fips.ap-south-1.amazonaws.com"},
        :fips-ap-east-1
        {:credentialScope {:region "ap-east-1"},
         :hostname "waf-regional-fips.ap-east-1.amazonaws.com"},
        :eu-west-2
        {:credentialScope {:region "eu-west-2"},
         :hostname "waf-regional.eu-west-2.amazonaws.com"},
        :fips-eu-west-3
        {:credentialScope {:region "eu-west-3"},
         :hostname "waf-regional-fips.eu-west-3.amazonaws.com"},
        :us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "waf-regional.us-west-1.amazonaws.com"},
        :fips-ap-southeast-1
        {:credentialScope {:region "ap-southeast-1"},
         :hostname "waf-regional-fips.ap-southeast-1.amazonaws.com"},
        :fips-ap-southeast-2
        {:credentialScope {:region "ap-southeast-2"},
         :hostname "waf-regional-fips.ap-southeast-2.amazonaws.com"},
        :fips-eu-west-2
        {:credentialScope {:region "eu-west-2"},
         :hostname "waf-regional-fips.eu-west-2.amazonaws.com"},
        :af-south-1
        {:credentialScope {:region "af-south-1"},
         :hostname "waf-regional.af-south-1.amazonaws.com"},
        :fips-af-south-1
        {:credentialScope {:region "af-south-1"},
         :hostname "waf-regional-fips.af-south-1.amazonaws.com"},
        :ap-southeast-1
        {:credentialScope {:region "ap-southeast-1"},
         :hostname "waf-regional.ap-southeast-1.amazonaws.com"},
        :us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "waf-regional.us-west-2.amazonaws.com"},
        :eu-south-1
        {:credentialScope {:region "eu-south-1"},
         :hostname "waf-regional.eu-south-1.amazonaws.com"},
        :fips-ap-northeast-1
        {:credentialScope {:region "ap-northeast-1"},
         :hostname "waf-regional-fips.ap-northeast-1.amazonaws.com"},
        :fips-ap-northeast-2
        {:credentialScope {:region "ap-northeast-2"},
         :hostname "waf-regional-fips.ap-northeast-2.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "waf-regional-fips.us-west-2.amazonaws.com"},
        :fips-eu-north-1
        {:credentialScope {:region "eu-north-1"},
         :hostname "waf-regional-fips.eu-north-1.amazonaws.com"},
        :eu-central-1
        {:credentialScope {:region "eu-central-1"},
         :hostname "waf-regional.eu-central-1.amazonaws.com"},
        :us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "waf-regional.us-east-1.amazonaws.com"},
        :eu-west-1
        {:credentialScope {:region "eu-west-1"},
         :hostname "waf-regional.eu-west-1.amazonaws.com"},
        :fips-eu-central-1
        {:credentialScope {:region "eu-central-1"},
         :hostname "waf-regional-fips.eu-central-1.amazonaws.com"},
        :eu-west-3
        {:credentialScope {:region "eu-west-3"},
         :hostname "waf-regional.eu-west-3.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "waf-regional-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "waf-regional-fips.ca-central-1.amazonaws.com"},
        :ap-east-1
        {:credentialScope {:region "ap-east-1"},
         :hostname "waf-regional.ap-east-1.amazonaws.com"},
        :ap-southeast-2
        {:credentialScope {:region "ap-southeast-2"},
         :hostname "waf-regional.ap-southeast-2.amazonaws.com"},
        :ap-south-1
        {:credentialScope {:region "ap-south-1"},
         :hostname "waf-regional.ap-south-1.amazonaws.com"},
        :me-south-1
        {:credentialScope {:region "me-south-1"},
         :hostname "waf-regional.me-south-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "waf-regional-fips.us-east-2.amazonaws.com"},
        :sa-east-1
        {:credentialScope {:region "sa-east-1"},
         :hostname "waf-regional.sa-east-1.amazonaws.com"},
        :fips-eu-south-1
        {:credentialScope {:region "eu-south-1"},
         :hostname "waf-regional-fips.eu-south-1.amazonaws.com"},
        :fips-eu-west-1
        {:credentialScope {:region "eu-west-1"},
         :hostname "waf-regional-fips.eu-west-1.amazonaws.com"}}},
      :api.ecr
      {:endpoints
       {:ap-northeast-1
        {:credentialScope {:region "ap-northeast-1"},
         :hostname "api.ecr.ap-northeast-1.amazonaws.com"},
        :ap-northeast-2
        {:credentialScope {:region "ap-northeast-2"},
         :hostname "api.ecr.ap-northeast-2.amazonaws.com"},
        :fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "ecr-fips.us-east-1.amazonaws.com"},
        :ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "api.ecr.ca-central-1.amazonaws.com"},
        :us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "api.ecr.us-east-2.amazonaws.com"},
        :eu-north-1
        {:credentialScope {:region "eu-north-1"},
         :hostname "api.ecr.eu-north-1.amazonaws.com"},
        :fips-dkr-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "ecr-fips.us-east-1.amazonaws.com"},
        :eu-west-2
        {:credentialScope {:region "eu-west-2"},
         :hostname "api.ecr.eu-west-2.amazonaws.com"},
        :us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "api.ecr.us-west-1.amazonaws.com"},
        :fips-dkr-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "ecr-fips.us-east-2.amazonaws.com"},
        :af-south-1
        {:credentialScope {:region "af-south-1"},
         :hostname "api.ecr.af-south-1.amazonaws.com"},
        :fips-dkr-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "ecr-fips.us-west-1.amazonaws.com"},
        :ap-southeast-1
        {:credentialScope {:region "ap-southeast-1"},
         :hostname "api.ecr.ap-southeast-1.amazonaws.com"},
        :us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "api.ecr.us-west-2.amazonaws.com"},
        :eu-south-1
        {:credentialScope {:region "eu-south-1"},
         :hostname "api.ecr.eu-south-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "ecr-fips.us-west-2.amazonaws.com"},
        :eu-central-1
        {:credentialScope {:region "eu-central-1"},
         :hostname "api.ecr.eu-central-1.amazonaws.com"},
        :us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "api.ecr.us-east-1.amazonaws.com"},
        :eu-west-1
        {:credentialScope {:region "eu-west-1"},
         :hostname "api.ecr.eu-west-1.amazonaws.com"},
        :eu-west-3
        {:credentialScope {:region "eu-west-3"},
         :hostname "api.ecr.eu-west-3.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "ecr-fips.us-west-1.amazonaws.com"},
        :ap-east-1
        {:credentialScope {:region "ap-east-1"},
         :hostname "api.ecr.ap-east-1.amazonaws.com"},
        :ap-southeast-2
        {:credentialScope {:region "ap-southeast-2"},
         :hostname "api.ecr.ap-southeast-2.amazonaws.com"},
        :ap-south-1
        {:credentialScope {:region "ap-south-1"},
         :hostname "api.ecr.ap-south-1.amazonaws.com"},
        :me-south-1
        {:credentialScope {:region "me-south-1"},
         :hostname "api.ecr.me-south-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "ecr-fips.us-east-2.amazonaws.com"},
        :sa-east-1
        {:credentialScope {:region "sa-east-1"},
         :hostname "api.ecr.sa-east-1.amazonaws.com"},
        :fips-dkr-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "ecr-fips.us-west-2.amazonaws.com"}}},
      :ds
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "ds-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "ds-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "ds-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "ds-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "ds-fips.us-east-2.amazonaws.com"}}},
      :acm-pca
      {:defaults {:protocols ["https"]},
       :endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "acm-pca-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "acm-pca-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "acm-pca-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "acm-pca-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "acm-pca-fips.us-east-2.amazonaws.com"}}},
      :ecs
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "ecs-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "ecs-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "ecs-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "ecs-fips.us-east-2.amazonaws.com"}}},
      :swf
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "swf-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "swf-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "swf-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "swf-fips.us-east-2.amazonaws.com"}}},
      :api.sagemaker
      {:endpoints
       {:us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "api-fips.sagemaker.us-east-2.amazonaws.com"},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "api-fips.sagemaker.us-west-2.amazonaws.com"},
        :us-west-1-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "api-fips.sagemaker.us-west-1.amazonaws.com"},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "api-fips.sagemaker.us-east-1.amazonaws.com"}}},
      :organizations
      {:endpoints
       {:aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "organizations.us-east-1.amazonaws.com"},
        :fips-aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "organizations-fips.us-east-1.amazonaws.com"}},
       :isRegionalized false,
       :partitionEndpoint "aws-global"},
      :metering.marketplace
      {:defaults {:credentialScope {:service "aws-marketplace"}}},
      :redshift
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "redshift-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "redshift-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "redshift-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "redshift-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "redshift-fips.us-east-2.amazonaws.com"}}},
      :batch
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "fips.batch.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "fips.batch.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "fips.batch.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "fips.batch.us-east-2.amazonaws.com"}}},
      :rds
      {:endpoints
       {:rds-fips.us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "rds-fips.us-west-2.amazonaws.com"},
        :rds-fips.us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "rds-fips.us-west-1.amazonaws.com"},
        :us-east-1 {:sslCommonName "{service}.{dnsSuffix}"},
        :rds-fips.us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "rds-fips.us-east-2.amazonaws.com"},
        :rds-fips.ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "rds-fips.ca-central-1.amazonaws.com"},
        :rds-fips.us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "rds-fips.us-east-1.amazonaws.com"}}},
      :s3-control
      {:defaults {:protocols ["https"], :signatureVersions ["s3v4"]},
       :endpoints
       {:ap-northeast-1
        {:credentialScope {:region "ap-northeast-1"},
         :hostname "s3-control.ap-northeast-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :ap-northeast-2
        {:credentialScope {:region "ap-northeast-2"},
         :hostname "s3-control.ap-northeast-2.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "s3-control.ca-central-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "s3-control.us-east-2.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :eu-north-1
        {:credentialScope {:region "eu-north-1"},
         :hostname "s3-control.eu-north-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "s3-control-fips.us-east-2.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :eu-west-2
        {:credentialScope {:region "eu-west-2"},
         :hostname "s3-control.eu-west-2.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "s3-control.us-west-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :ap-southeast-1
        {:credentialScope {:region "ap-southeast-1"},
         :hostname "s3-control.ap-southeast-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "s3-control.us-west-2.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :ca-central-1-fips
        {:credentialScope {:region "ca-central-1"},
         :hostname "s3-control-fips.ca-central-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :eu-central-1
        {:credentialScope {:region "eu-central-1"},
         :hostname "s3-control.eu-central-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "s3-control.us-east-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :eu-west-1
        {:credentialScope {:region "eu-west-1"},
         :hostname "s3-control.eu-west-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "s3-control-fips.us-west-2.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :eu-west-3
        {:credentialScope {:region "eu-west-3"},
         :hostname "s3-control.eu-west-3.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :us-west-1-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "s3-control-fips.us-west-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :ap-southeast-2
        {:credentialScope {:region "ap-southeast-2"},
         :hostname "s3-control.ap-southeast-2.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :ap-south-1
        {:credentialScope {:region "ap-south-1"},
         :hostname "s3-control.ap-south-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :sa-east-1
        {:credentialScope {:region "sa-east-1"},
         :hostname "s3-control.sa-east-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "s3-control-fips.us-east-1.amazonaws.com",
         :signatureVersions ["s3v4"]}}},
      :savingsplans
      {:endpoints
       {:aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "savingsplans.amazonaws.com"}},
       :isRegionalized false,
       :partitionEndpoint "aws-global"},
      :macie2
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "macie2-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "macie2-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "macie2-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "macie2-fips.us-east-2.amazonaws.com"}}},
      :securityhub
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "securityhub-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "securityhub-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "securityhub-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "securityhub-fips.us-east-2.amazonaws.com"}}},
      :acm
      {:endpoints
       {:us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "acm-fips.us-east-2.amazonaws.com"},
        :ca-central-1-fips
        {:credentialScope {:region "ca-central-1"},
         :hostname "acm-fips.ca-central-1.amazonaws.com"},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "acm-fips.us-west-2.amazonaws.com"},
        :us-west-1-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "acm-fips.us-west-1.amazonaws.com"},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "acm-fips.us-east-1.amazonaws.com"}}},
      :servicequotas {:defaults {:protocols ["https"]}},
      :docdb
      {:endpoints
       {:ap-northeast-1
        {:credentialScope {:region "ap-northeast-1"},
         :hostname "rds.ap-northeast-1.amazonaws.com"},
        :ap-northeast-2
        {:credentialScope {:region "ap-northeast-2"},
         :hostname "rds.ap-northeast-2.amazonaws.com"},
        :ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "rds.ca-central-1.amazonaws.com"},
        :us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "rds.us-east-2.amazonaws.com"},
        :eu-west-2
        {:credentialScope {:region "eu-west-2"},
         :hostname "rds.eu-west-2.amazonaws.com"},
        :ap-southeast-1
        {:credentialScope {:region "ap-southeast-1"},
         :hostname "rds.ap-southeast-1.amazonaws.com"},
        :us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "rds.us-west-2.amazonaws.com"},
        :eu-central-1
        {:credentialScope {:region "eu-central-1"},
         :hostname "rds.eu-central-1.amazonaws.com"},
        :us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "rds.us-east-1.amazonaws.com"},
        :eu-west-1
        {:credentialScope {:region "eu-west-1"},
         :hostname "rds.eu-west-1.amazonaws.com"},
        :eu-west-3
        {:credentialScope {:region "eu-west-3"},
         :hostname "rds.eu-west-3.amazonaws.com"},
        :ap-southeast-2
        {:credentialScope {:region "ap-southeast-2"},
         :hostname "rds.ap-southeast-2.amazonaws.com"},
        :ap-south-1
        {:credentialScope {:region "ap-south-1"},
         :hostname "rds.ap-south-1.amazonaws.com"}}},
      :elasticbeanstalk
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "elasticbeanstalk-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "elasticbeanstalk-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "elasticbeanstalk-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "elasticbeanstalk-fips.us-east-2.amazonaws.com"}}},
      :secretsmanager
      {:endpoints
       {:us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "secretsmanager-fips.us-east-2.amazonaws.com"},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "secretsmanager-fips.us-west-2.amazonaws.com"},
        :us-west-1-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "secretsmanager-fips.us-west-1.amazonaws.com"},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "secretsmanager-fips.us-east-1.amazonaws.com"}}},
      :servicecatalog
      {:endpoints
       {:us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "servicecatalog-fips.us-east-2.amazonaws.com"},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "servicecatalog-fips.us-west-2.amazonaws.com"},
        :us-west-1-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "servicecatalog-fips.us-west-1.amazonaws.com"},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "servicecatalog-fips.us-east-1.amazonaws.com"}}},
      :neptune
      {:endpoints
       {:ap-northeast-1
        {:credentialScope {:region "ap-northeast-1"},
         :hostname "rds.ap-northeast-1.amazonaws.com"},
        :ap-northeast-2
        {:credentialScope {:region "ap-northeast-2"},
         :hostname "rds.ap-northeast-2.amazonaws.com"},
        :ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "rds.ca-central-1.amazonaws.com"},
        :us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "rds.us-east-2.amazonaws.com"},
        :eu-north-1
        {:credentialScope {:region "eu-north-1"},
         :hostname "rds.eu-north-1.amazonaws.com"},
        :eu-west-2
        {:credentialScope {:region "eu-west-2"},
         :hostname "rds.eu-west-2.amazonaws.com"},
        :us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "rds.us-west-1.amazonaws.com"},
        :ap-southeast-1
        {:credentialScope {:region "ap-southeast-1"},
         :hostname "rds.ap-southeast-1.amazonaws.com"},
        :us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "rds.us-west-2.amazonaws.com"},
        :eu-central-1
        {:credentialScope {:region "eu-central-1"},
         :hostname "rds.eu-central-1.amazonaws.com"},
        :us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "rds.us-east-1.amazonaws.com"},
        :eu-west-1
        {:credentialScope {:region "eu-west-1"},
         :hostname "rds.eu-west-1.amazonaws.com"},
        :eu-west-3
        {:credentialScope {:region "eu-west-3"},
         :hostname "rds.eu-west-3.amazonaws.com"},
        :ap-east-1
        {:credentialScope {:region "ap-east-1"},
         :hostname "rds.ap-east-1.amazonaws.com"},
        :ap-southeast-2
        {:credentialScope {:region "ap-southeast-2"},
         :hostname "rds.ap-southeast-2.amazonaws.com"},
        :ap-south-1
        {:credentialScope {:region "ap-south-1"},
         :hostname "rds.ap-south-1.amazonaws.com"},
        :me-south-1
        {:credentialScope {:region "me-south-1"},
         :hostname "rds.me-south-1.amazonaws.com"},
        :sa-east-1
        {:credentialScope {:region "sa-east-1"},
         :hostname "rds.sa-east-1.amazonaws.com"}}},
      :sts
      {:endpoints
       {:aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "sts.amazonaws.com"},
        :us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "sts-fips.us-east-2.amazonaws.com"},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "sts-fips.us-west-2.amazonaws.com"},
        :us-west-1-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "sts-fips.us-west-1.amazonaws.com"},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "sts-fips.us-east-1.amazonaws.com"}},
       :partitionEndpoint "aws-global"},
      :route53
      {:endpoints
       {:aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "route53.amazonaws.com"},
        :fips-aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "route53-fips.amazonaws.com"}},
       :isRegionalized false,
       :partitionEndpoint "aws-global"},
      :directconnect
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "directconnect-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "directconnect-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "directconnect-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "directconnect-fips.us-east-2.amazonaws.com"}}},
      :api.elastic-inference
      {:endpoints
       {:ap-northeast-1
        {:hostname "api.elastic-inference.ap-northeast-1.amazonaws.com"},
        :ap-northeast-2
        {:hostname "api.elastic-inference.ap-northeast-2.amazonaws.com"},
        :eu-west-1
        {:hostname "api.elastic-inference.eu-west-1.amazonaws.com"},
        :us-east-1
        {:hostname "api.elastic-inference.us-east-1.amazonaws.com"},
        :us-east-2
        {:hostname "api.elastic-inference.us-east-2.amazonaws.com"},
        :us-west-2
        {:hostname "api.elastic-inference.us-west-2.amazonaws.com"}}},
      :serverlessrepo
      {:defaults {:protocols ["https"]},
       :endpoints
       {:ap-northeast-1 {:protocols ["https"]},
        :ap-northeast-2 {:protocols ["https"]},
        :ca-central-1 {:protocols ["https"]},
        :us-east-2 {:protocols ["https"]},
        :eu-north-1 {:protocols ["https"]},
        :eu-west-2 {:protocols ["https"]},
        :us-west-1 {:protocols ["https"]},
        :ap-southeast-1 {:protocols ["https"]},
        :us-west-2 {:protocols ["https"]},
        :eu-central-1 {:protocols ["https"]},
        :us-east-1 {:protocols ["https"]},
        :eu-west-1 {:protocols ["https"]},
        :eu-west-3 {:protocols ["https"]},
        :ap-east-1 {:protocols ["https"]},
        :ap-southeast-2 {:protocols ["https"]},
        :ap-south-1 {:protocols ["https"]},
        :me-south-1 {:protocols ["https"]},
        :sa-east-1 {:protocols ["https"]}}},
      :dynamodb
      {:defaults {:protocols ["http" "https"]},
       :endpoints
       {:us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "dynamodb-fips.us-east-2.amazonaws.com"},
        :ca-central-1-fips
        {:credentialScope {:region "ca-central-1"},
         :hostname "dynamodb-fips.ca-central-1.amazonaws.com"},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "dynamodb-fips.us-west-2.amazonaws.com"},
        :us-west-1-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "dynamodb-fips.us-west-1.amazonaws.com"},
        :local
        {:credentialScope {:region "us-east-1"},
         :hostname "localhost:8000",
         :protocols ["http"]},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "dynamodb-fips.us-east-1.amazonaws.com"}}},
      :elasticfilesystem
      {:endpoints
       {:fips-sa-east-1
        {:credentialScope {:region "sa-east-1"},
         :hostname "elasticfilesystem-fips.sa-east-1.amazonaws.com"},
        :fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "elasticfilesystem-fips.us-east-1.amazonaws.com"},
        :fips-me-south-1
        {:credentialScope {:region "me-south-1"},
         :hostname "elasticfilesystem-fips.me-south-1.amazonaws.com"},
        :fips-ap-south-1
        {:credentialScope {:region "ap-south-1"},
         :hostname "elasticfilesystem-fips.ap-south-1.amazonaws.com"},
        :fips-ap-east-1
        {:credentialScope {:region "ap-east-1"},
         :hostname "elasticfilesystem-fips.ap-east-1.amazonaws.com"},
        :fips-eu-west-3
        {:credentialScope {:region "eu-west-3"},
         :hostname "elasticfilesystem-fips.eu-west-3.amazonaws.com"},
        :fips-ap-southeast-1
        {:credentialScope {:region "ap-southeast-1"},
         :hostname
         "elasticfilesystem-fips.ap-southeast-1.amazonaws.com"},
        :fips-ap-southeast-2
        {:credentialScope {:region "ap-southeast-2"},
         :hostname
         "elasticfilesystem-fips.ap-southeast-2.amazonaws.com"},
        :fips-eu-west-2
        {:credentialScope {:region "eu-west-2"},
         :hostname "elasticfilesystem-fips.eu-west-2.amazonaws.com"},
        :fips-af-south-1
        {:credentialScope {:region "af-south-1"},
         :hostname "elasticfilesystem-fips.af-south-1.amazonaws.com"},
        :fips-ap-northeast-1
        {:credentialScope {:region "ap-northeast-1"},
         :hostname
         "elasticfilesystem-fips.ap-northeast-1.amazonaws.com"},
        :fips-ap-northeast-2
        {:credentialScope {:region "ap-northeast-2"},
         :hostname
         "elasticfilesystem-fips.ap-northeast-2.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "elasticfilesystem-fips.us-west-2.amazonaws.com"},
        :fips-eu-north-1
        {:credentialScope {:region "eu-north-1"},
         :hostname "elasticfilesystem-fips.eu-north-1.amazonaws.com"},
        :fips-eu-central-1
        {:credentialScope {:region "eu-central-1"},
         :hostname "elasticfilesystem-fips.eu-central-1.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "elasticfilesystem-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "elasticfilesystem-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "elasticfilesystem-fips.us-east-2.amazonaws.com"},
        :fips-eu-south-1
        {:credentialScope {:region "eu-south-1"},
         :hostname "elasticfilesystem-fips.eu-south-1.amazonaws.com"},
        :fips-eu-west-1
        {:credentialScope {:region "eu-west-1"},
         :hostname "elasticfilesystem-fips.eu-west-1.amazonaws.com"}}},
      :cloudfront
      {:endpoints
       {:aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "cloudfront.amazonaws.com",
         :protocols ["http" "https"]}},
       :isRegionalized false,
       :partitionEndpoint "aws-global"},
      :access-analyzer
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "access-analyzer-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "access-analyzer-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "access-analyzer-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "access-analyzer-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "access-analyzer-fips.us-east-2.amazonaws.com"}}},
      :sms
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "sms-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "sms-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "sms-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "sms-fips.us-east-2.amazonaws.com"}}},
      :translate
      {:defaults {:protocols ["https"]},
       :endpoints
       {:us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "translate-fips.us-east-2.amazonaws.com"},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "translate-fips.us-west-2.amazonaws.com"},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "translate-fips.us-east-1.amazonaws.com"}}},
      :health
      {:endpoints
       {:fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "health-fips.us-east-2.amazonaws.com"}}},
      :resource-groups
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "resource-groups-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "resource-groups-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "resource-groups-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "resource-groups-fips.us-east-2.amazonaws.com"}}},
      :route53resolver {:defaults {:protocols ["https"]}},
      :cognito-idp
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "cognito-idp-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "cognito-idp-fips.us-west-2.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "cognito-idp-fips.us-east-2.amazonaws.com"}}},
      :mturk-requester
      {:endpoints
       {:sandbox
        {:hostname "mturk-requester-sandbox.us-east-1.amazonaws.com"}},
       :isRegionalized false},
      :api.pricing {:defaults {:credentialScope {:service "pricing"}}},
      :iotthingsgraph
      {:defaults {:credentialScope {:service "iotthingsgraph"}}},
      :transcribe
      {:defaults {:protocols ["https"]},
       :endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "fips.transcribe.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "fips.transcribe.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "fips.transcribe.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "fips.transcribe.us-east-2.amazonaws.com"}}},
      :iam
      {:endpoints
       {:aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "iam.amazonaws.com"},
        :iam-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "iam-fips.amazonaws.com"}},
       :isRegionalized false,
       :partitionEndpoint "aws-global"},
      :comprehendmedical
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "comprehendmedical-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "comprehendmedical-fips.us-west-2.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "comprehendmedical-fips.us-east-2.amazonaws.com"}}},
      :servicediscovery
      {:endpoints
       {:servicediscovery-fips
        {:credentialScope {:region "ca-central-1"},
         :hostname "servicediscovery-fips.ca-central-1.amazonaws.com"}}},
      :sdb
      {:defaults
       {:protocols ["http" "https"], :signatureVersions ["v2"]},
       :endpoints {:us-east-1 {:hostname "sdb.amazonaws.com"}}},
      :portal.sso
      {:endpoints
       {:ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "portal.sso.ca-central-1.amazonaws.com"},
        :us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "portal.sso.us-east-2.amazonaws.com"},
        :eu-west-2
        {:credentialScope {:region "eu-west-2"},
         :hostname "portal.sso.eu-west-2.amazonaws.com"},
        :ap-southeast-1
        {:credentialScope {:region "ap-southeast-1"},
         :hostname "portal.sso.ap-southeast-1.amazonaws.com"},
        :us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "portal.sso.us-west-2.amazonaws.com"},
        :eu-central-1
        {:credentialScope {:region "eu-central-1"},
         :hostname "portal.sso.eu-central-1.amazonaws.com"},
        :us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "portal.sso.us-east-1.amazonaws.com"},
        :eu-west-1
        {:credentialScope {:region "eu-west-1"},
         :hostname "portal.sso.eu-west-1.amazonaws.com"},
        :ap-southeast-2
        {:credentialScope {:region "ap-southeast-2"},
         :hostname "portal.sso.ap-southeast-2.amazonaws.com"}}},
      :macie
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "macie-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "macie-fips.us-west-2.amazonaws.com"}}},
      :greengrass
      {:defaults {:protocols ["https"]}, :isRegionalized true},
      :codedeploy
      {:endpoints
       {:us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "codedeploy-fips.us-east-2.amazonaws.com"},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "codedeploy-fips.us-west-2.amazonaws.com"},
        :us-west-1-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "codedeploy-fips.us-west-1.amazonaws.com"},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "codedeploy-fips.us-east-1.amazonaws.com"}}},
      :pinpoint
      {:defaults {:credentialScope {:service "mobiletargeting"}},
       :endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "pinpoint-fips.us-east-1.amazonaws.com"},
        :us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "pinpoint.us-west-2.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "pinpoint-fips.us-west-2.amazonaws.com"},
        :us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "pinpoint.us-east-1.amazonaws.com"}}},
      :models.lex {:defaults {:credentialScope {:service "lex"}}},
      :appstream2
      {:defaults
       {:credentialScope {:service "appstream"}, :protocols ["https"]},
       :endpoints
       {:fips
        {:credentialScope {:region "us-west-2"},
         :hostname "appstream2-fips.us-west-2.amazonaws.com"}}},
      :datasync
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "datasync-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "datasync-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "datasync-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "datasync-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "datasync-fips.us-east-2.amazonaws.com"}}},
      :workspaces
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "workspaces-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "workspaces-fips.us-west-2.amazonaws.com"}}},
      :chime
      {:defaults
       {:protocols ["https"],
        :sslCommonName "service.chime.aws.amazon.com"},
       :endpoints
       {:aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "service.chime.aws.amazon.com",
         :protocols ["https"]}},
       :isRegionalized false,
       :partitionEndpoint "aws-global"},
      :entitlement.marketplace
      {:defaults {:credentialScope {:service "aws-marketplace"}}},
      :data.iot
      {:defaults
       {:credentialScope {:service "iotdata"}, :protocols ["https"]}},
      :s3
      {:defaults
       {:protocols ["http" "https"], :signatureVersions ["s3v4"]},
       :endpoints
       {:ap-northeast-1
        {:hostname "s3.ap-northeast-1.amazonaws.com",
         :signatureVersions ["s3" "s3v4"]},
        :aws-global
        {:credentialScope {:region "us-east-1"},
         :hostname "s3.amazonaws.com",
         :signatureVersions ["s3" "s3v4"]},
        :us-west-1
        {:hostname "s3.us-west-1.amazonaws.com",
         :signatureVersions ["s3" "s3v4"]},
        :s3-external-1
        {:credentialScope {:region "us-east-1"},
         :hostname "s3-external-1.amazonaws.com",
         :signatureVersions ["s3" "s3v4"]},
        :us-east-1-regional
        {:signatureVersions ["s3" "s3v4"],
         :hostname "s3.us-east-1.amazonaws.com"},
        :ap-southeast-1
        {:hostname "s3.ap-southeast-1.amazonaws.com",
         :signatureVersions ["s3" "s3v4"]},
        :us-west-2
        {:hostname "s3.us-west-2.amazonaws.com",
         :signatureVersions ["s3" "s3v4"]},
        :us-east-1
        {:signatureVersions ["s3" "s3v4"], :hostname "s3.amazonaws.com"},
        :eu-west-1
        {:hostname "s3.eu-west-1.amazonaws.com",
         :signatureVersions ["s3" "s3v4"]},
        :ap-southeast-2
        {:hostname "s3.ap-southeast-2.amazonaws.com",
         :signatureVersions ["s3" "s3v4"]},
        :sa-east-1
        {:hostname "s3.sa-east-1.amazonaws.com",
         :signatureVersions ["s3" "s3v4"]}},
       :isRegionalized true,
       :partitionEndpoint "aws-global"},
      :importexport
      {:endpoints
       {:aws-global
        {:credentialScope
         {:region "us-east-1", :service "IngestionService"},
         :hostname "importexport.amazonaws.com",
         :signatureVersions ["v2" "v4"]}},
       :isRegionalized false,
       :partitionEndpoint "aws-global"},
      :elasticache
      {:endpoints
       {:fips
        {:credentialScope {:region "us-west-1"},
         :hostname "elasticache-fips.us-west-1.amazonaws.com"}}},
      :polly
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "polly-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "polly-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "polly-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "polly-fips.us-east-2.amazonaws.com"}}},
      :runtime.lex {:defaults {:credentialScope {:service "lex"}}},
      :snowball
      {:endpoints
       {:fips-sa-east-1
        {:credentialScope {:region "sa-east-1"},
         :hostname "snowball-fips.sa-east-1.amazonaws.com"},
        :fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "snowball-fips.us-east-1.amazonaws.com"},
        :fips-ap-south-1
        {:credentialScope {:region "ap-south-1"},
         :hostname "snowball-fips.ap-south-1.amazonaws.com"},
        :fips-eu-west-3
        {:credentialScope {:region "eu-west-3"},
         :hostname "snowball-fips.eu-west-3.amazonaws.com"},
        :fips-ap-southeast-1
        {:credentialScope {:region "ap-southeast-1"},
         :hostname "snowball-fips.ap-southeast-1.amazonaws.com"},
        :fips-ap-southeast-2
        {:credentialScope {:region "ap-southeast-2"},
         :hostname "snowball-fips.ap-southeast-2.amazonaws.com"},
        :fips-eu-west-2
        {:credentialScope {:region "eu-west-2"},
         :hostname "snowball-fips.eu-west-2.amazonaws.com"},
        :fips-ap-northeast-1
        {:credentialScope {:region "ap-northeast-1"},
         :hostname "snowball-fips.ap-northeast-1.amazonaws.com"},
        :fips-ap-northeast-3
        {:credentialScope {:region "ap-northeast-3"},
         :hostname "snowball-fips.ap-northeast-3.amazonaws.com"},
        :fips-ap-northeast-2
        {:credentialScope {:region "ap-northeast-2"},
         :hostname "snowball-fips.ap-northeast-2.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "snowball-fips.us-west-2.amazonaws.com"},
        :fips-eu-central-1
        {:credentialScope {:region "eu-central-1"},
         :hostname "snowball-fips.eu-central-1.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "snowball-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "snowball-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "snowball-fips.us-east-2.amazonaws.com"},
        :fips-eu-west-1
        {:credentialScope {:region "eu-west-1"},
         :hostname "snowball-fips.eu-west-1.amazonaws.com"}}},
      :kinesis
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "kinesis-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "kinesis-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "kinesis-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "kinesis-fips.us-east-2.amazonaws.com"}}},
      :outposts
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "outposts-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "outposts-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "outposts-fips.us-west-1.amazonaws.com"},
        :fips-ca-central-1
        {:credentialScope {:region "ca-central-1"},
         :hostname "outposts-fips.ca-central-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "outposts-fips.us-east-2.amazonaws.com"}}},
      :firehose
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "firehose-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "firehose-fips.us-west-2.amazonaws.com"},
        :fips-us-west-1
        {:credentialScope {:region "us-west-1"},
         :hostname "firehose-fips.us-west-1.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "firehose-fips.us-east-2.amazonaws.com"}}},
      :cognito-identity
      {:endpoints
       {:fips-us-east-1
        {:credentialScope {:region "us-east-1"},
         :hostname "cognito-identity-fips.us-east-1.amazonaws.com"},
        :fips-us-west-2
        {:credentialScope {:region "us-west-2"},
         :hostname "cognito-identity-fips.us-west-2.amazonaws.com"},
        :fips-us-east-2
        {:credentialScope {:region "us-east-2"},
         :hostname "cognito-identity-fips.us-east-2.amazonaws.com"}}},
      :codebuild
      {:endpoints
       {:us-east-2-fips
        {:credentialScope {:region "us-east-2"},
         :hostname "codebuild-fips.us-east-2.amazonaws.com"},
        :us-west-2-fips
        {:credentialScope {:region "us-west-2"},
         :hostname "codebuild-fips.us-west-2.amazonaws.com"},
        :us-west-1-fips
        {:credentialScope {:region "us-west-1"},
         :hostname "codebuild-fips.us-west-1.amazonaws.com"},
        :us-east-1-fips
        {:credentialScope {:region "us-east-1"},
         :hostname "codebuild-fips.us-east-1.amazonaws.com"}}}}}
    {:defaults
     {:hostname "{service}.{region}.{dnsSuffix}",
      :protocols ["https"],
      :signatureVersions ["v4"]},
     :dnsSuffix "amazonaws.com.cn",
     :partition "aws-cn",
     :partitionName "AWS China",
     :regionRegex "^cn\\-\\w+\\-\\d+$",
     :regions
     {:cn-north-1 {:description "China (Beijing)"},
      :cn-northwest-1 {:description "China (Ningxia)"}},
     :services
     {:ioteventsdata
      {:endpoints
       {:cn-north-1
        {:credentialScope {:region "cn-north-1"},
         :hostname "data.iotevents.cn-north-1.amazonaws.com.cn"}}},
      :eks {:defaults {:protocols ["http" "https"]}},
      :budgets
      {:endpoints
       {:aws-cn-global
        {:credentialScope {:region "cn-northwest-1"},
         :hostname "budgets.amazonaws.com.cn"}},
       :isRegionalized false,
       :partitionEndpoint "aws-cn-global"},
      :monitoring {:defaults {:protocols ["http" "https"]}},
      :ec2 {:defaults {:protocols ["http" "https"]}},
      :elasticloadbalancing {:defaults {:protocols ["https"]}},
      :application-autoscaling {:defaults {:protocols ["http" "https"]}},
      :autoscaling-plans {:defaults {:protocols ["http" "https"]}},
      :ce
      {:endpoints
       {:aws-cn-global
        {:credentialScope {:region "cn-northwest-1"},
         :hostname "ce.cn-northwest-1.amazonaws.com.cn"}},
       :isRegionalized false,
       :partitionEndpoint "aws-cn-global"},
      :glacier {:defaults {:protocols ["http" "https"]}},
      :sns {:defaults {:protocols ["http" "https"]}},
      :autoscaling {:defaults {:protocols ["http" "https"]}},
      :mediaconvert
      {:endpoints
       {:cn-northwest-1
        {:credentialScope {:region "cn-northwest-1"},
         :hostname
         "subscribe.mediaconvert.cn-northwest-1.amazonaws.com.cn"}}},
      :streams.dynamodb
      {:defaults
       {:credentialScope {:service "dynamodb"},
        :protocols ["http" "https"]}},
      :elasticmapreduce {:defaults {:protocols ["https"]}},
      :support
      {:endpoints
       {:aws-cn-global
        {:credentialScope {:region "cn-north-1"},
         :hostname "support.cn-north-1.amazonaws.com.cn"}},
       :partitionEndpoint "aws-cn-global"},
      :sqs
      {:defaults
       {:protocols ["http" "https"],
        :sslCommonName "{region}.queue.{dnsSuffix}"}},
      :iot {:defaults {:credentialScope {:service "execute-api"}}},
      :api.ecr
      {:endpoints
       {:cn-north-1
        {:credentialScope {:region "cn-north-1"},
         :hostname "api.ecr.cn-north-1.amazonaws.com.cn"},
        :cn-northwest-1
        {:credentialScope {:region "cn-northwest-1"},
         :hostname "api.ecr.cn-northwest-1.amazonaws.com.cn"}}},
      :organizations
      {:endpoints
       {:aws-cn-global
        {:credentialScope {:region "cn-northwest-1"},
         :hostname "organizations.cn-northwest-1.amazonaws.com.cn"},
        :fips-aws-cn-global
        {:credentialScope {:region "cn-northwest-1"},
         :hostname "organizations.cn-northwest-1.amazonaws.com.cn"}},
       :isRegionalized false,
       :partitionEndpoint "aws-cn-global"},
      :s3-control
      {:defaults {:protocols ["https"], :signatureVersions ["s3v4"]},
       :endpoints
       {:cn-north-1
        {:credentialScope {:region "cn-north-1"},
         :hostname "s3-control.cn-north-1.amazonaws.com.cn",
         :signatureVersions ["s3v4"]},
        :cn-northwest-1
        {:credentialScope {:region "cn-northwest-1"},
         :hostname "s3-control.cn-northwest-1.amazonaws.com.cn",
         :signatureVersions ["s3v4"]}}},
      :neptune
      {:endpoints
       {:cn-northwest-1
        {:credentialScope {:region "cn-northwest-1"},
         :hostname "rds.cn-northwest-1.amazonaws.com.cn"}}},
      :route53
      {:endpoints
       {:aws-cn-global
        {:credentialScope {:region "cn-northwest-1"},
         :hostname "route53.amazonaws.com.cn"}},
       :isRegionalized false,
       :partitionEndpoint "aws-cn-global"},
      :serverlessrepo
      {:defaults {:protocols ["https"]},
       :endpoints
       {:cn-north-1 {:protocols ["https"]},
        :cn-northwest-1 {:protocols ["https"]}}},
      :dynamodb {:defaults {:protocols ["http" "https"]}},
      :elasticfilesystem
      {:endpoints
       {:fips-cn-north-1
        {:credentialScope {:region "cn-north-1"},
         :hostname "elasticfilesystem-fips.cn-north-1.amazonaws.com.cn"},
        :fips-cn-northwest-1
        {:credentialScope {:region "cn-northwest-1"},
         :hostname
         "elasticfilesystem-fips.cn-northwest-1.amazonaws.com.cn"}}},
      :cloudfront
      {:endpoints
       {:aws-cn-global
        {:credentialScope {:region "cn-northwest-1"},
         :hostname "cloudfront.cn-northwest-1.amazonaws.com.cn",
         :protocols ["http" "https"]}},
       :isRegionalized false,
       :partitionEndpoint "aws-cn-global"},
      :transcribe
      {:defaults {:protocols ["https"]},
       :endpoints
       {:cn-north-1
        {:credentialScope {:region "cn-north-1"},
         :hostname "cn.transcribe.cn-north-1.amazonaws.com.cn"},
        :cn-northwest-1
        {:credentialScope {:region "cn-northwest-1"},
         :hostname "cn.transcribe.cn-northwest-1.amazonaws.com.cn"}}},
      :iam
      {:endpoints
       {:aws-cn-global
        {:credentialScope {:region "cn-north-1"},
         :hostname "iam.cn-north-1.amazonaws.com.cn"}},
       :isRegionalized false,
       :partitionEndpoint "aws-cn-global"},
      :greengrass
      {:defaults {:protocols ["https"]}, :isRegionalized true},
      :data.iot
      {:defaults
       {:credentialScope {:service "iotdata"}, :protocols ["https"]}},
      :s3
      {:defaults
       {:protocols ["http" "https"], :signatureVersions ["s3v4"]}},
      :snowball
      {:endpoints
       {:fips-cn-north-1
        {:credentialScope {:region "cn-north-1"},
         :hostname "snowball-fips.cn-north-1.amazonaws.com.cn"},
        :fips-cn-northwest-1
        {:credentialScope {:region "cn-northwest-1"},
         :hostname "snowball-fips.cn-northwest-1.amazonaws.com.cn"}}}}}
    {:defaults
     {:hostname "{service}.{region}.{dnsSuffix}",
      :protocols ["https"],
      :signatureVersions ["v4"]},
     :dnsSuffix "amazonaws.com",
     :partition "aws-us-gov",
     :partitionName "AWS GovCloud (US)",
     :regionRegex "^us\\-gov\\-\\w+\\-\\d+$",
     :regions
     {:us-gov-east-1 {:description "AWS GovCloud (US-East)"},
      :us-gov-west-1 {:description "AWS GovCloud (US-West)"}},
     :services
     {:eks {:defaults {:protocols ["http" "https"]}},
      :monitoring
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "monitoring.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "monitoring.us-gov-west-1.amazonaws.com"}}},
      :comprehend
      {:defaults {:protocols ["https"]},
       :endpoints
       {:fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "comprehend-fips.us-gov-west-1.amazonaws.com"}}},
      :ec2
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "ec2.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "ec2.us-gov-west-1.amazonaws.com"}}},
      :guardduty
      {:defaults {:protocols ["https"]},
       :endpoints
       {:us-gov-west-1-fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "guardduty.us-gov-west-1.amazonaws.com"}},
       :isRegionalized true},
      :email
      {:endpoints
       {:fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "email-fips.us-gov-west-1.amazonaws.com"}}},
      :elasticloadbalancing
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "elasticloadbalancing.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "elasticloadbalancing.us-gov-west-1.amazonaws.com"},
        :us-gov-west-1 {:protocols ["http" "https"]}}},
      :cloudformation
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "cloudformation.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "cloudformation.us-gov-west-1.amazonaws.com"}}},
      :es
      {:endpoints
       {:fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "es-fips.us-gov-west-1.amazonaws.com"}}},
      :codepipeline
      {:endpoints
       {:fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "codepipeline-fips.us-gov-west-1.amazonaws.com"}}},
      :cloudtrail
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "cloudtrail.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "cloudtrail.us-gov-west-1.amazonaws.com"}}},
      :application-autoscaling
      {:defaults {:protocols ["http" "https"]},
       :endpoints
       {:us-gov-east-1 {:protocols ["http" "https"]},
        :us-gov-west-1 {:protocols ["http" "https"]}}},
      :autoscaling-plans
      {:defaults {:protocols ["http" "https"]},
       :endpoints
       {:us-gov-east-1 {:protocols ["http" "https"]},
        :us-gov-west-1 {:protocols ["http" "https"]}}},
      :dms
      {:endpoints
       {:dms-fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "dms.us-gov-west-1.amazonaws.com"}}},
      :glacier
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "glacier.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "glacier.us-gov-west-1.amazonaws.com",
         :protocols ["http" "https"]}}},
      :sns
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "sns.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "sns.us-gov-west-1.amazonaws.com",
         :protocols ["http" "https"]}}},
      :autoscaling
      {:endpoints
       {:us-gov-east-1 {:protocols ["http" "https"]},
        :us-gov-west-1 {:protocols ["http" "https"]}}},
      :states
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "states-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "states.us-gov-west-1.amazonaws.com"}}},
      :license-manager
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "license-manager-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "license-manager-fips.us-gov-west-1.amazonaws.com"}}},
      :storagegateway
      {:endpoints
       {:fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "storagegateway-fips.us-gov-west-1.amazonaws.com"}}},
      :glue
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "glue-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "glue-fips.us-gov-west-1.amazonaws.com"}}},
      :ssm
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "ssm.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "ssm.us-gov-west-1.amazonaws.com"}}},
      :lambda
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "lambda-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "lambda-fips.us-gov-west-1.amazonaws.com"}}},
      :codecommit
      {:endpoints
       {:fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "codecommit-fips.us-gov-west-1.amazonaws.com"}}},
      :mediaconvert
      {:endpoints
       {:us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "mediaconvert.us-gov-west-1.amazonaws.com"}}},
      :streams.dynamodb
      {:defaults {:credentialScope {:service "dynamodb"}},
       :endpoints
       {:us-gov-east-1-fips
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "dynamodb.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1-fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "dynamodb.us-gov-west-1.amazonaws.com"}}},
      :logs
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "logs.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "logs.us-gov-west-1.amazonaws.com"}}},
      :inspector
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "inspector-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "inspector-fips.us-gov-west-1.amazonaws.com"}}},
      :transfer
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "transfer-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "transfer-fips.us-gov-west-1.amazonaws.com"}}},
      :elasticmapreduce
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "elasticmapreduce.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "elasticmapreduce.us-gov-west-1.amazonaws.com"},
        :us-gov-west-1 {:protocols ["https"]}}},
      :support
      {:endpoints
       {:aws-us-gov-global
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "support.us-gov-west-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "support.us-gov-west-1.amazonaws.com"}},
       :partitionEndpoint "aws-us-gov-global"},
      :events
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "events.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "events.us-gov-west-1.amazonaws.com"}}},
      :cloudhsmv2 {:defaults {:credentialScope {:service "cloudhsm"}}},
      :rekognition
      {:endpoints
       {:rekognition-fips.us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "rekognition-fips.us-gov-west-1.amazonaws.com"}}},
      :sqs
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "sqs.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "sqs.us-gov-west-1.amazonaws.com",
         :protocols ["http" "https"],
         :sslCommonName "{region}.queue.{dnsSuffix}"}}},
      :iot {:defaults {:credentialScope {:service "execute-api"}}},
      :waf-regional
      {:endpoints
       {:fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "waf-regional-fips.us-gov-west-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "waf-regional.us-gov-west-1.amazonaws.com"}}},
      :api.ecr
      {:endpoints
       {:fips-dkr-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "ecr-fips.us-gov-east-1.amazonaws.com"},
        :fips-dkr-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "ecr-fips.us-gov-west-1.amazonaws.com"},
        :fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "ecr-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "ecr-fips.us-gov-west-1.amazonaws.com"},
        :us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "api.ecr.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "api.ecr.us-gov-west-1.amazonaws.com"}}},
      :ds
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "ds-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "ds-fips.us-gov-west-1.amazonaws.com"}}},
      :acm-pca
      {:defaults {:protocols ["https"]},
       :endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "acm-pca.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "acm-pca.us-gov-west-1.amazonaws.com"}}},
      :ecs
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "ecs-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "ecs-fips.us-gov-west-1.amazonaws.com"}}},
      :swf
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "swf.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "swf.us-gov-west-1.amazonaws.com"}}},
      :api.sagemaker
      {:endpoints
       {:us-gov-west-1-fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "api-fips.sagemaker.us-gov-west-1.amazonaws.com"},
        :us-gov-west-1-fips-secondary
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "api.sagemaker.us-gov-west-1.amazonaws.com"}}},
      :organizations
      {:endpoints
       {:aws-us-gov-global
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "organizations.us-gov-west-1.amazonaws.com"},
        :fips-aws-us-gov-global
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "organizations.us-gov-west-1.amazonaws.com"}},
       :isRegionalized false,
       :partitionEndpoint "aws-us-gov-global"},
      :metering.marketplace
      {:defaults {:credentialScope {:service "aws-marketplace"}}},
      :redshift
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "redshift.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "redshift.us-gov-west-1.amazonaws.com"}}},
      :batch
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "batch.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "batch.us-gov-west-1.amazonaws.com"}}},
      :rds
      {:endpoints
       {:rds.us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "rds.us-gov-east-1.amazonaws.com"},
        :rds.us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "rds.us-gov-west-1.amazonaws.com"}}},
      :s3-control
      {:defaults {:protocols ["https"], :signatureVersions ["s3v4"]},
       :endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "s3-control.us-gov-east-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :us-gov-east-1-fips
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "s3-control-fips.us-gov-east-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "s3-control.us-gov-west-1.amazonaws.com",
         :signatureVersions ["s3v4"]},
        :us-gov-west-1-fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "s3-control-fips.us-gov-west-1.amazonaws.com",
         :signatureVersions ["s3v4"]}}},
      :securityhub
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "securityhub-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "securityhub-fips.us-gov-west-1.amazonaws.com"}}},
      :acm
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "acm.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "acm.us-gov-west-1.amazonaws.com"}}},
      :docdb
      {:endpoints
       {:us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "rds.us-gov-west-1.amazonaws.com"}}},
      :elasticbeanstalk
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "elasticbeanstalk.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "elasticbeanstalk.us-gov-west-1.amazonaws.com"}}},
      :secretsmanager
      {:endpoints
       {:us-gov-east-1-fips
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "secretsmanager-fips.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1-fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "secretsmanager-fips.us-gov-west-1.amazonaws.com"}}},
      :servicecatalog
      {:endpoints
       {:us-gov-east-1-fips
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "servicecatalog-fips.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1-fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "servicecatalog-fips.us-gov-west-1.amazonaws.com"}}},
      :neptune
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "rds.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "rds.us-gov-west-1.amazonaws.com"}}},
      :sts
      {:endpoints
       {:us-gov-east-1-fips
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "sts.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1-fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "sts.us-gov-west-1.amazonaws.com"}}},
      :athena
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "athena-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "athena-fips.us-gov-west-1.amazonaws.com"}}},
      :route53
      {:endpoints
       {:aws-us-gov-global
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "route53.us-gov.amazonaws.com"},
        :fips-aws-us-gov-global
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "route53.us-gov.amazonaws.com"}},
       :isRegionalized false,
       :partitionEndpoint "aws-us-gov-global"},
      :directconnect
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "directconnect.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "directconnect.us-gov-west-1.amazonaws.com"}}},
      :serverlessrepo
      {:defaults {:protocols ["https"]},
       :endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "serverlessrepo.us-gov-east-1.amazonaws.com",
         :protocols ["https"]},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "serverlessrepo.us-gov-west-1.amazonaws.com",
         :protocols ["https"]}}},
      :dynamodb
      {:endpoints
       {:us-gov-east-1-fips
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "dynamodb.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1-fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "dynamodb.us-gov-west-1.amazonaws.com"}}},
      :elasticfilesystem
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "elasticfilesystem-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname
         "elasticfilesystem-fips.us-gov-west-1.amazonaws.com"}}},
      :access-analyzer
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "access-analyzer.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "access-analyzer.us-gov-west-1.amazonaws.com"}}},
      :sms
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "sms-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "sms-fips.us-gov-west-1.amazonaws.com"}}},
      :translate
      {:defaults {:protocols ["https"]},
       :endpoints
       {:us-gov-west-1-fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "translate-fips.us-gov-west-1.amazonaws.com"}}},
      :health
      {:endpoints
       {:fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "health-fips.us-gov-west-1.amazonaws.com"}}},
      :resource-groups
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "resource-groups.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "resource-groups.us-gov-west-1.amazonaws.com"}}},
      :cognito-idp
      {:endpoints
       {:fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "cognito-idp-fips.us-gov-west-1.amazonaws.com"}}},
      :transcribe
      {:defaults {:protocols ["https"]},
       :endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "fips.transcribe.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "fips.transcribe.us-gov-west-1.amazonaws.com"}}},
      :iam
      {:endpoints
       {:aws-us-gov-global
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "iam.us-gov.amazonaws.com"},
        :iam-govcloud-fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "iam.us-gov.amazonaws.com"}},
       :isRegionalized false,
       :partitionEndpoint "aws-us-gov-global"},
      :comprehendmedical
      {:endpoints
       {:fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname
         "comprehendmedical-fips.us-gov-west-1.amazonaws.com"}}},
      :greengrass
      {:defaults {:protocols ["https"]},
       :endpoints
       {:dataplane-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "greengrass-ats.iot.us-gov-west-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "greengrass.us-gov-west-1.amazonaws.com"}},
       :isRegionalized true},
      :codedeploy
      {:endpoints
       {:us-gov-east-1-fips
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "codedeploy-fips.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1-fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "codedeploy-fips.us-gov-west-1.amazonaws.com"}}},
      :pinpoint
      {:defaults {:credentialScope {:service "mobiletargeting"}},
       :endpoints
       {:fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "pinpoint-fips.us-gov-west-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "pinpoint.us-gov-west-1.amazonaws.com"}}},
      :appstream2
      {:defaults
       {:credentialScope {:service "appstream"}, :protocols ["https"]},
       :endpoints
       {:fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "appstream2-fips.us-gov-west-1.amazonaws.com"}}},
      :datasync
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "datasync-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "datasync-fips.us-gov-west-1.amazonaws.com"}}},
      :workspaces
      {:endpoints
       {:fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "workspaces-fips.us-gov-west-1.amazonaws.com"}}},
      :kms
      {:endpoints
       {:ProdFips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "kms-fips.us-gov-west-1.amazonaws.com"}}},
      :data.iot
      {:defaults
       {:credentialScope {:service "iotdata"}, :protocols ["https"]}},
      :s3
      {:defaults {:signatureVersions ["s3" "s3v4"]},
       :endpoints
       {:fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "s3-fips.us-gov-west-1.amazonaws.com"},
        :us-gov-east-1
        {:hostname "s3.us-gov-east-1.amazonaws.com",
         :protocols ["http" "https"]},
        :us-gov-west-1
        {:hostname "s3.us-gov-west-1.amazonaws.com",
         :protocols ["http" "https"]}}},
      :elasticache
      {:endpoints
       {:fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "elasticache.us-gov-west-1.amazonaws.com"}}},
      :polly
      {:endpoints
       {:fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "polly-fips.us-gov-west-1.amazonaws.com"}}},
      :snowball
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "snowball-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "snowball-fips.us-gov-west-1.amazonaws.com"}}},
      :kinesis
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "kinesis.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "kinesis.us-gov-west-1.amazonaws.com"}}},
      :outposts
      {:endpoints
       {:us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "outposts.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "outposts.us-gov-west-1.amazonaws.com"}}},
      :firehose
      {:endpoints
       {:fips-us-gov-east-1
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "firehose-fips.us-gov-east-1.amazonaws.com"},
        :fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "firehose-fips.us-gov-west-1.amazonaws.com"}}},
      :cognito-identity
      {:endpoints
       {:fips-us-gov-west-1
        {:credentialScope {:region "us-gov-west-1"},
         :hostname
         "cognito-identity-fips.us-gov-west-1.amazonaws.com"}}},
      :codebuild
      {:endpoints
       {:us-gov-east-1-fips
        {:credentialScope {:region "us-gov-east-1"},
         :hostname "codebuild-fips.us-gov-east-1.amazonaws.com"},
        :us-gov-west-1-fips
        {:credentialScope {:region "us-gov-west-1"},
         :hostname "codebuild-fips.us-gov-west-1.amazonaws.com"}}}}}
    {:defaults
     {:hostname "{service}.{region}.{dnsSuffix}",
      :protocols ["https"],
      :signatureVersions ["v4"]},
     :dnsSuffix "c2s.ic.gov",
     :partition "aws-iso",
     :partitionName "AWS ISO (US)",
     :regionRegex "^us\\-iso\\-\\w+\\-\\d+$",
     :regions {:us-iso-east-1 {:description "US ISO East"}},
     :services
     {:comprehend {:defaults {:protocols ["https"]}},
      :elasticloadbalancing
      {:endpoints {:us-iso-east-1 {:protocols ["http" "https"]}}},
      :application-autoscaling {:defaults {:protocols ["http" "https"]}},
      :dms
      {:endpoints
       {:dms-fips
        {:credentialScope {:region "us-iso-east-1"},
         :hostname "dms.us-iso-east-1.c2s.ic.gov"}}},
      :glacier
      {:endpoints {:us-iso-east-1 {:protocols ["http" "https"]}}},
      :sns {:endpoints {:us-iso-east-1 {:protocols ["http" "https"]}}},
      :autoscaling
      {:endpoints {:us-iso-east-1 {:protocols ["http" "https"]}}},
      :streams.dynamodb
      {:defaults
       {:credentialScope {:service "dynamodb"},
        :protocols ["http" "https"]},
       :endpoints {:us-iso-east-1 {:protocols ["http" "https"]}}},
      :elasticmapreduce
      {:endpoints {:us-iso-east-1 {:protocols ["https"]}}},
      :support
      {:endpoints
       {:aws-iso-global
        {:credentialScope {:region "us-iso-east-1"},
         :hostname "support.us-iso-east-1.c2s.ic.gov"}},
       :partitionEndpoint "aws-iso-global"},
      :sqs {:endpoints {:us-iso-east-1 {:protocols ["http" "https"]}}},
      :api.ecr
      {:endpoints
       {:us-iso-east-1
        {:credentialScope {:region "us-iso-east-1"},
         :hostname "api.ecr.us-iso-east-1.c2s.ic.gov"}}},
      :route53
      {:endpoints
       {:aws-iso-global
        {:credentialScope {:region "us-iso-east-1"},
         :hostname "route53.c2s.ic.gov"}},
       :isRegionalized false,
       :partitionEndpoint "aws-iso-global"},
      :dynamodb
      {:endpoints {:us-iso-east-1 {:protocols ["http" "https"]}}},
      :transcribe {:defaults {:protocols ["https"]}},
      :iam
      {:endpoints
       {:aws-iso-global
        {:credentialScope {:region "us-iso-east-1"},
         :hostname "iam.us-iso-east-1.c2s.ic.gov"}},
       :isRegionalized false,
       :partitionEndpoint "aws-iso-global"},
      :kms
      {:endpoints
       {:ProdFips
        {:credentialScope {:region "us-iso-east-1"},
         :hostname "kms-fips.us-iso-east-1.c2s.ic.gov"}}},
      :s3
      {:defaults {:signatureVersions ["s3v4"]},
       :endpoints
       {:us-iso-east-1
        {:protocols ["http" "https"], :signatureVersions ["s3v4"]}}}}}
    {:defaults
     {:hostname "{service}.{region}.{dnsSuffix}",
      :protocols ["https"],
      :signatureVersions ["v4"]},
     :dnsSuffix "sc2s.sgov.gov",
     :partition "aws-iso-b",
     :partitionName "AWS ISOB (US)",
     :regionRegex "^us\\-isob\\-\\w+\\-\\d+$",
     :regions {:us-isob-east-1 {:description "US ISOB East (Ohio)"}},
     :services
     {:ec2 {:defaults {:protocols ["http" "https"]}},
      :elasticloadbalancing
      {:endpoints {:us-isob-east-1 {:protocols ["https"]}}},
      :application-autoscaling {:defaults {:protocols ["http" "https"]}},
      :dms
      {:endpoints
       {:dms-fips
        {:credentialScope {:region "us-isob-east-1"},
         :hostname "dms.us-isob-east-1.sc2s.sgov.gov"}}},
      :sns {:defaults {:protocols ["http" "https"]}},
      :autoscaling {:defaults {:protocols ["http" "https"]}},
      :streams.dynamodb
      {:defaults
       {:credentialScope {:service "dynamodb"},
        :protocols ["http" "https"]}},
      :support
      {:endpoints
       {:aws-iso-b-global
        {:credentialScope {:region "us-isob-east-1"},
         :hostname "support.us-isob-east-1.sc2s.sgov.gov"}},
       :partitionEndpoint "aws-iso-b-global"},
      :sqs
      {:defaults
       {:protocols ["http" "https"],
        :sslCommonName "{region}.queue.{dnsSuffix}"}},
      :dynamodb {:defaults {:protocols ["http" "https"]}},
      :iam
      {:endpoints
       {:aws-iso-b-global
        {:credentialScope {:region "us-isob-east-1"},
         :hostname "iam.us-isob-east-1.sc2s.sgov.gov"}},
       :isRegionalized false,
       :partitionEndpoint "aws-iso-b-global"},
      :kms
      {:endpoints
       {:ProdFips
        {:credentialScope {:region "us-isob-east-1"},
         :hostname "kms-fips.us-isob-east-1.sc2s.sgov.gov"}}},
      :s3
      {:defaults
       {:protocols ["http" "https"], :signatureVersions ["s3v4"]}}}}],
   :version 3})

(defn set-endpoints [data] (reset! endpoints data))

(defn render-template
  [template args]
  (str/replace template
               #"\{([^}]+)\}"
               #(get args (second %))))

(defn service-resolve
  "Resolve the endpoint for the given service."
  [partition service-name service region-key]
  (let [endpoint (get-in service [:endpoints region-key])
        region   (name region-key)
        result   (merge (:defaults partition)
                        (:defaults service)
                        endpoint
                        {:partition (:partition partition)
                         :region    region
                         :dnsSuffix (:dnsSuffix partition)})]
    (util/map-vals #(render-template % {"service"   service-name
                                        "region"    region
                                        "dnsSuffix" (:dnsSuffix partition)})
                   result
                   [:hostname :sslCommonName])))

(defn partition-resolve
  [{:keys [services] :as partition} service-key region-key]
  (when (contains? (-> partition :regions keys set) region-key)
    (let [{:keys [partitionEndpoint isRegionalized] :as service} (get services service-key)
          endpoint-key (if (and partitionEndpoint (not isRegionalized))
                         (keyword partitionEndpoint)
                         region-key)]
      (service-resolve partition (name service-key) service endpoint-key))))

(defn resolve*
  "Resolves an endpoint for a given service and region.

  service keyword Identify a AWS service (e.g. :s3)
  region keyword  Identify a AWS region (e.g. :us-east-1).

  Return a map with the following keys:

  :partition            The name of the partition.
  :region               The region of the endpoint.
  :hostname             The hostname to use.
  :ssl-common-name      The ssl-common-name to use (optional).
  :credential-scope     The Signature v4 credential scope (optional).
  :signature-versions   A list of possible signature versions (optional).
  :protocols            A list of supported protocols."
  [service-key region]
  (if-let [{:keys [partitions]} endpoints]
    (some #(partition-resolve % service-key region) partitions)
    (throw (ex-info "Endpoints are not defined" {:service-key service-key
                                                 :region region}))))

(def resolve (memoize resolve*))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defprotocol EndpointProvider
  (-fetch [provider region]))

(defn default-endpoint-provider [api endpointPrefix endpoint-override]
  (reify EndpointProvider
    (-fetch [_ region]
      (if-let [ep (resolve (keyword endpointPrefix) (keyword region))]
        (merge ep (if (string? endpoint-override)
                    {:hostname endpoint-override}
                    endpoint-override))
        {:cognitect.anomalies/category :cognitect.anomalies/fault
         :cognitect.anomalies/message "No known endpoint."}))))

(defn fetch [provider region]
  (-fetch provider region))
