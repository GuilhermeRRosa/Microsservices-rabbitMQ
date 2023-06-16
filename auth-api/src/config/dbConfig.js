import { Sequelize } from "sequelize";

const sequelize = new Sequelize("auth-db", "admin", "root", {
    host: "localhost",
    dialect: "postgres",
    quoteIdentifiers: false,
    define: {
        syncOnAssociation: true,
        timestamps: false,
        underscored: false,
        underscoredAll: false,
        freezeTableName: true
    }
});

sequelize.authenticate().then(() => {
    console.info("Connection to auth-db has been stablished!");
}).catch((err) => {
    console.error("Unable to connect to auth-db!");
    console.error(err.message);
})

export default sequelize;
