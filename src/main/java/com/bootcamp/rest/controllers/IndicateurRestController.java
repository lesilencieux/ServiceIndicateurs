/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bootcamp.rest.controllers;

import com.bootcamp.entities.Indicateur;
import com.bootcamp.enums.TypeIndicateur;
import com.bootcamp.service.crud.IndicateurCRUD;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author root
 */
@Path("/indicateurs")
@Api(value = "Indicateur", description = "web service on indicateur")
public class IndicateurRestController {

    //   IndicateurRepository ir = new IndicateurRepository("punit-mysql");
    Indicateur indicateur = new Indicateur();
    List<Indicateur> indicateurs = new ArrayList<>();

//Cette methode cree une nouvelle instance de indicateur et la retourne
    @GET
    @Path("/unpersist_list")
    @Produces("application/json")
    @ApiOperation(value = "Visualise un  indicateur non enregistre {de test}   ")
    public Response getList() {
        Indicateur indicateur1 = new Indicateur("libelle1", "nom1", "nature1", "propriete1", TypeIndicateur.QUALITATIF, "valeur1");

        return Response.status(200).entity(indicateur1).build();
    }

    /*
    *
    *La methode ci-dessous  cree une instance de IndicateurPerformanceRepository (ir) , 
    *applique sur cette derniere la methode findAll de cette classe
    *puis essaie de retourner la liste des indicateurs persistés.
    *si tout ce passe bien la liste est retournee si l'exeption est levee 
    *et le message d'erreur est retournee dans le catch.
    * Cette liste est accessible avec URI /list
    *
     */
    @GET
    @Path("/list")
    @Produces("application/json")
    @ApiOperation(value = "Visualise la liste des indicateurs   ")
    public Response getListIndicateurFromDB() {
        try {


            indicateurs = IndicateurCRUD.readAll();
            return Response.status(200).entity(indicateurs).build();
        } catch (Exception e) {
            return Response.status(404).entity("Erreur ! Veuillez revoir l' URL et reessayez").build();
        }
    }

    /*
    *
    *La methode  ci-dessous cree une instance de IndicateurPerformanceRepository (ir) , 
    *applique sur cette derniere la methode findById de la classe
    *pour retourner le indicateur   persisté avec l'indentifiant specifié dans 
    *l'URI d'acces qui est /list/id ou id est l'identifiant.
    *
     */
    @GET
    @Path("/{id}")
    @Produces("application/json")
    @ApiOperation(value = "Visualise un indicateur en donnant son id ")
    public Response getIndicateurPerformanceByIdFromDB(@PathParam("id") int id) throws SQLException {
        indicateur = IndicateurCRUD.read(id);
        try {
            //Verification de l'existance de l'id dans la base de donnee

            if (indicateur == null) {
                return Response.status(500).entity("Aucun indicateur ne correspond a l'indifiant indique").build();
            } else {
                return Response.status(200).entity(indicateur).build();
            }
        } catch (Exception e) {
            return Response.status(401).entity("Veuillez verifier si c'est bien un URL correcte qque vous entrez").build();
        }
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Cree un nouvel indicateurs   ")
    public Response createBene(Indicateur indicateur) throws SQLException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        if (checkEntitie(indicateur)) {
            return Response.status(202).entity("Cette entite existe deja").build();
        } else {
            IndicateurCRUD.create(indicateur);
            return Response.status(404).entity("Entite est cree est succes").build();
        }

    }

    @PUT
    @Path("/update")
    @ApiOperation(value = "Met a jour un indicateur ou le cree si ce la existait pas   ")
    public Response updateBene(Indicateur indicateur) throws SQLException {

        IndicateurCRUD.update(indicateur);
        return Response.status(202).entity("l'entite est bien mise a jour ou cree s'il n'existait pas").build();

    }

    @DELETE
    @Path("/delete/{id}")
    @ApiOperation(value = "Supprime un indicateur en  donnant son id  ")
    public Response deleteBene(@PathParam("id") int id) throws SQLException {
        Indicateur b = IndicateurCRUD.read(id);
        try {
            IndicateurCRUD.delete(b);
            return Response.status(202).entity("l'entite est supprime est succes").build();
        } catch (Exception e) {
            return Response.status(404).entity("Erreur de suppression de l'entite").build();
        }
    }

    @GET//Signifie qu il s agit d une methode de lecture 
    @ApiOperation(value = "Visulise l indicateur de id {id} avec seulement les champs specifies  ")
    @Path("/attribut/essentiels/{id}")//Avec cette URI on donnera seulement les attributs que l on souhaite afficher pour un bailleur donne
    @Produces(MediaType.APPLICATION_JSON)//Production de JSON
    public Response getByIdParam(@PathParam("id") int id, @QueryParam("fields") String fields) throws SQLException, IllegalArgumentException, IllegalAccessException, IntrospectionException, InvocationTargetException {
        if (fields.isEmpty()) {
            return Response.status(200).entity("vous devez specifier un attribut au moins").build();
        } else {
            HashMap<String, Object> responseMap = magik(id, fields);
            return Response.status(200).entity(responseMap).build();
        }

    }

    @GET
    @Path("/rechercher")
    @Produces("application/json")
    @ApiOperation(value = "Recherche les  indicateurs ayant pour attribut {attribut} et la value de l atrtribut {value}  ")
    public Response SearcheIndicateur(@QueryParam("attribut") String attribut, @QueryParam("value") String value) throws IntrospectionException, SQLException {

        List<PropertyDescriptor> propertyDescriptors = IndicateurCRUD.returnAskedProperties(Indicateur.class, attribut);

        if (attribut.isEmpty()) {
            return Response.status(200).entity("vous devez specifier l attribut sur lequel la recherche va se faire").build();
        } else {
            if (!propertyDescriptors.isEmpty()) {
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    indicateurs = IndicateurCRUD.search(attribut, value);
                }
            }

        }

        return Response.status(200).entity(indicateur).build();
    }

    @GET
    @Path("/paginer")
    @ApiOperation(value = "Retourne une liste paginer d indicateurs par defaut on 2 et maximum par page est 25 ")
    @Produces("application/json")
    public Response findPagingBailleur(@QueryParam("offset")
            @DefaultValue("2") Integer offset, @QueryParam("limit") @DefaultValue("25") Integer limit) {
        return Response.status(200).entity(IndicateurCRUD.pager(offset, limit)).build();
    }

    @GET//Signifie que c est une methode de lecture
    @Path("/trier")//URI pour avoir la liste des Bailleurs tries suivant un attribut donne
    @Produces("application/json")//Permet de dire a la methode quelle va produire du JSON
    @ApiOperation(value = "Retourne une liste triee de d indicateur  suivant l attribut specifie")
    public Response triBailleur(@QueryParam("sort") String attribut) throws SQLException {
        List<Indicateur> bail = IndicateurCRUD.readAll();//Recuperation de tous les indicateurs
        //Comparaison de la liste retournee
        Collections.sort(bail, new Comparator<Indicateur>() {
            @Override
            public int compare(Indicateur indicateurPerformance1, Indicateur indicateurPerformance2) {
                int result = 0;
                List<PropertyDescriptor> propertyDescriptors;
                try {
                    //La methode returnAskedPropertiesIfExist verifie si l attribut specifie
                    //par l utilisateur fait partir des proprietse de la classe
                    //Si oui la liste d attribut
                    propertyDescriptors = IndicateurCRUD.returnAskedProperties(Indicateur.class, attribut);
                    if (!propertyDescriptors.isEmpty()) { //Verifions si la liste retournee n est vide
                        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

                            //Suivant le nom de l attribut on compare pour le tri
                            switch (propertyDescriptor.getName()) {
                                case "nom":
                                    result = indicateurPerformance1.getNom().compareToIgnoreCase(indicateurPerformance2.getNom());
                                    break;
                                case "TypeBailleur":
                                    result = indicateurPerformance1.getTypeIndicateur().compareTo(indicateurPerformance2.getTypeIndicateur());
                                    break;
                                default:
                                    result = 0;
                                    break;
                            }
                        }
                    }
                } catch (IntrospectionException | SQLException ex) {
                    Logger.getLogger(IndicateurRestController.class.getName()).log(Level.SEVERE, null, ex);
                }

                return result;
            }
        });

        return Response.status(200).entity(bail).build();
    }

    private Boolean existe(Indicateur new_indicateur) throws SQLException {
        boolean b = false;
        indicateurs = IndicateurCRUD.readAll();
        for (Indicateur curent_indicateur : indicateurs) {
            b = new_indicateur.equals(curent_indicateur);
        }
        return b;
    }

    /**
     * Cette methode selection l entite de l identifiant est egale id si elle
     * exite puis verifie si les attributs demandes sur l attribut existent si
     * oui l entite est renvoyee
     *
     */
    private HashMap magik(int id, String flds) throws IntrospectionException, SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        HashMap<String, Object> responseMap = new HashMap<>();
        indicateur = IndicateurCRUD.read(id);
        List<PropertyDescriptor> propertyDescriptors = IndicateurCRUD.returnAskedProperties(Indicateur.class, flds);

        if (!(propertyDescriptors.isEmpty())) {
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method method = propertyDescriptor.getReadMethod();
                responseMap.put(propertyDescriptor.getName(), method.invoke(indicateur));
            }
        }
        return responseMap;
    }

    private boolean checkEntitie(Indicateur indicateur) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {

        boolean b = false;
        indicateurs = IndicateurCRUD.readAll();

        for (Indicateur curent_indicateur : indicateurs) {
            if (indicateur.getLibelle().equalsIgnoreCase(curent_indicateur.getLibelle())
                    && indicateur.getNature().equalsIgnoreCase(curent_indicateur.getNature())
                    && indicateur.getNom().equalsIgnoreCase(curent_indicateur.getNom())
                    && indicateur.getPropriete().equalsIgnoreCase(curent_indicateur.getPropriete())
                    && indicateur.getTypeIndicateur().equals(curent_indicateur.getTypeIndicateur())
                    && indicateur.getValeur().equalsIgnoreCase(curent_indicateur.getValeur())) {
                b = true;
            }
        }

        return b;
    }
}
