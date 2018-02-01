(ns status-im.test.profile.events
  (:require [cljs.test :refer [deftest is testing]]
            reagent.core
            [re-frame.core :as rf]
            [day8.re-frame.test :refer [run-test-sync]]
            status-im.ui.screens.db
            [status-im.ui.screens.events :as events]
            [status-im.ui.screens.profile.events :as profile-events]
            [status-im.ui.screens.accounts.events :as account-events]))

(def new-account
  {:address             "c296367a939e0957500a25ca89b70bd64b03004e"
   :signed-up?          true
   :name                "Disloyal Trusting Rainbowfish"
   :updates-private-key "3849071831f581f5e2a4f095a53e0a697144b32ea6de9e92cc08936f2efa40d2f1702bdb131356df0930a3a0d301221f2b5"
   :updates-public-key  "38453ecc298b8b35de00c85d3217f00aa7040a7d3053dbbf6831d03c750df40b27977906692b3b5d6fec8134706b2bf65900c61130047488520cb60080a59b118cb281f3aaf65ba704c7efde8f9357d2b22fe8110b38a4dd714c1c9e108a8b067fe"
   :photo-path          "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAMAAAC7IEhfAAADAFBMVEXw8PDYjLoAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAG2YFqAAABAHRSTlP//wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKmfXxgAABnNJREFUeNoBaAaX+QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAQEBAQEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEBAQEBAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQEBAQEBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAQEBAQEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEBAQEBAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQEBAQEBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAQEBAQEAAAAAAAABAQEBAQEAAAAAAAABAQEBAQEAAAAAAAAAAAAAAAEBAQEBAQAAAAAAAAEBAQEBAQAAAAAAAAEBAQEBAQAAAAAAAAAAAAAAAQEBAQEBAAAAAAAAAQEBAQEBAAAAAAAAAQEBAQEBAAAAAAAAAAAAAAABAQEBAQEAAAAAAAABAQEBAQEAAAAAAAABAQEBAQEAAAAAAAAAAAAAAAEBAQEBAQAAAAAAAAEBAQEBAQAAAAAAAAEBAQEBAQAAAAAAAAAAAAAAAQEBAQEBAAAAAAAAAQEBAQEBAAAAAAAAAQEBAQEBAAAAAAAAAAAAAAAAAAAAAAABAQEBAQEBAQEBAQEBAQEBAQEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEBAQEBAQEBAQEBAQEBAQEBAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQEBAQEBAQEBAQEBAQEBAQEBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAQEBAQEBAQEBAQEBAQEBAQEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEBAQEBAQEBAQEBAQEBAQEBAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQEBAQEBAQEBAQEBAQEBAQEBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAQEBAQEBAQEBAQEBAQEBAQEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEBAQEBAQEBAQEBAQEBAQEBAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQEBAQEBAQEBAQEBAQEBAQEBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAQEBAQEBAQEBAQEBAQEBAQEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEBAQEBAQEBAQEBAQEBAQEBAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQEBAQEBAQEBAQEBAQEBAQEBAAAAAAAAAAAAAAAAAAAAAAABAQEBAQEAAAAAAAAAAAAAAAAAAAAAAAABAQEBAQEAAAAAAAAAAAAAAAEBAQEBAQAAAAAAAAAAAAAAAAAAAAAAAAEBAQEBAQAAAAAAAAAAAAAAAQEBAQEBAAAAAAAAAAAAAAAAAAAAAAAAAQEBAQEBAAAAAAAAAAAAAAABAQEBAQEAAAAAAAAAAAAAAAAAAAAAAAABAQEBAQEAAAAAAAAAAAAAAAEBAQEBAQAAAAAAAAAAAAAAAAAAAAAAAAEBAQEBAQAAAAAAAAAAAAAAAQEBAQEBAAAAAAAAAAAAAAAAAAAAAAAAAQEBAQEBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKUMBsXBa1BAAAAAASUVORK5CYII="
   :status              "the future starts today, not tomorrow"
   :signing-phrase      "baby atom limo"
   :public-key          "0x04f5722fba79eb36d73263417531007f43d13af76c6233573a8e3e60f667710611feba0785d751b50609bfc0b7cef35448875c5392c0a91948c95798a0ce600847"})


(defn test-fixtures []
  (rf/reg-fx ::events/init-store #())
  (rf/reg-fx :data-store.accounts/save #()))

(deftest profile-edit-events
  (run-test-sync
    (test-fixtures)
    (let [account (rf/subscribe [:get-current-account])
          address (:address new-account)]
      (rf/dispatch [:initialize-db])
      (rf/dispatch [:add-account new-account])
      (rf/dispatch [:initialize-account-db address])

      (testing "Setting status from edit profile screen"
        (let [new-status "New edit profile status"]
          (is (= (:status new-account) (:status @account)))
          (rf/dispatch [:my-profile/edit-profile :edit-status])
          (rf/dispatch [:my-profile/update-status new-status])
          (rf/dispatch [:my-profile/save-profile])
          (is (= new-status (:status @account)))))

      (testing "Setting status from drawer"
        (let [new-status "New drawer status"]
          (rf/dispatch [:my-profile.drawer/edit-status])
          (rf/dispatch [:my-profile.drawer/update-status new-status])
          (rf/dispatch [:my-profile.drawer/save-status])
          (is (= new-status (:status @account))))))))

(deftest test-status-change
  (let [fx {:db {}}]
    (is (= (profile-events/status-change fx {:old-status "this is old status"
                                             :status     "this is new and CHANGED status"})
           {:db         {}
            :dispatch-n [[:broadcast-status "this is new and CHANGED status"]]}))
    (is (= (profile-events/status-change fx {:old-status "this is old status"
                                             :status     "this is new and #changed status"})
           {:db         {}
            :dispatch-n [[:broadcast-status "this is new and #changed status"]]}))
    (is (= (profile-events/status-change fx {:old-status "this is old status"
                                             :status     "this is old status"})
           {:db {}}))))
